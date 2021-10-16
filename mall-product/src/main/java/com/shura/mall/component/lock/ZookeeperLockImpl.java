package com.shura.mall.component.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Garvey
 * @Created: 2021/10/16
 * @Description:
 */
@Slf4j
public class ZookeeperLockImpl implements ZookeeperLock, InitializingBean {

    private final static String LOCK_ROOT_PATH = "/ZookeeperLock";

    private Map<String, CountDownLatch> concurrentMap = new ConcurrentHashMap<>();

    private ReentrantLock lock = new ReentrantLock();

    @Autowired
    private CuratorFramework curatorFramework;

    @Override
    public boolean lock(String lockPath) {
        boolean result = false;
        String keyPath = LOCK_ROOT_PATH + lockPath;

        try {
            curatorFramework.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath(keyPath);
            result = true;
            log.info("success to acquire mutex lock for path: {}", keyPath);
        } catch (Exception e) {
            log.info("Thread: {}; failed to acquire mutex lock for path: {}", Thread.currentThread().getName(), keyPath);

            if (!concurrentMap.containsKey(lockPath)) {
                try {
                    /**
                     * 考虑到是高并发场景，必须保证对同一个节点加锁的线程失败后是落在同一个 countDown 对象上，
                     * 否则，有的线程就永远无法唤醒了。
                     */
                    lock.lock();
                    // 双重检验
                    if (!concurrentMap.containsKey(lockPath)) {
                        concurrentMap.put(lockPath, new CountDownLatch(1));
                    }
                } finally {
                    lock.unlock();
                }
            }

            try {
                CountDownLatch countDownLatch = concurrentMap.get(lockPath);
                if (countDownLatch != null) {
                    countDownLatch.await();
                }
            } catch (InterruptedException e1) {
                log.info("InterruptedException message: {}", e1.getMessage());
            }
        }

        return result;
    }

    @Override
    public boolean unlock(String lockPath) {
        String keyPath = LOCK_ROOT_PATH + lockPath;
        try {
            if (curatorFramework.checkExists().forPath(keyPath) != null) {
                curatorFramework.delete().forPath(keyPath);
            }
        } catch (Exception e) {
            log.error("failed to release mutex lock: {}", lockPath);
            return false;
        }

        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        curatorFramework = curatorFramework.usingNamespace("zklock-namespace");
        // zk 锁的根路径，不存在则创建
        try {
            if (curatorFramework.checkExists().forPath(LOCK_ROOT_PATH) == null) {
                curatorFramework
                        .create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath(LOCK_ROOT_PATH);
            }

            // 启动监听器
            addWatcher(LOCK_ROOT_PATH);
        } catch (Exception e) {
            log.error("connect zookeeper failed: {}", e.getMessage(), e);
        }
    }

    /**
     * 监听节点事件
     * @param lockPath 加锁路径
     * @throws Exception
     */
    private void addWatcher(String lockPath) throws Exception {
        String keyPath;
        if (LOCK_ROOT_PATH.equals(lockPath)) {
            keyPath = lockPath;
        } else {
            keyPath = LOCK_ROOT_PATH + lockPath;
        }

        final PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework, keyPath, false);
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

        // 添加监听器
        childrenCache.getListenable().addListener((client, event) -> {
            if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                String oldPath = event.getData().getPath();
                log.info("oldPath delete: {}, redis 缓存已更新！", oldPath);
                if (oldPath.contains(lockPath)) {
                    // TODO 释放计数器，释放锁
                    CountDownLatch countDownLatch = concurrentMap.remove(oldPath);
                    if (countDownLatch != null) { // 可能没有竞争，countDown 不存在
                        countDownLatch.countDown();
                    }
                }
            }
        });
    }
}

package com.shura.mall.component.lock;

/**
 * @Author: Garvey
 * @Created: 2021/10/16
 * @Description:
 */
public interface ZookeeperLock {

    boolean lock(String lockPath);

    boolean unlock(String lockPath);
}

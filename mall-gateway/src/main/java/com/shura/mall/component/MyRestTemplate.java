package com.shura.mall.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;

/**
 * @Author: Garvey
 * @Created: 2021/11/13
 * @Description: 自定义 RestTemplate
 */
@Slf4j
public class MyRestTemplate extends RestTemplate {

    private final DiscoveryClient discoveryClient;

    public MyRestTemplate(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    protected  <T> T doExecute(URI url, @Nullable HttpMethod method, @Nullable RequestCallback requestCallback,
                               @Nullable ResponseExtractor<T> responseExtractor) throws RestClientException {
        Assert.notNull(url, "URI is required");
        Assert.notNull(method, "HttpMethod is required");

        ClientHttpResponse response = null;
        try {
            // 判断 url 的拦截路径，然后去 redis（作为注册中心）获取地址随机选取一个
            log.info("请求的 url 路径为：{}", url);
            url = replaceUrl(url);
            log.info("替换后的 url 路径为：{}", url);

            ClientHttpRequest request = createRequest(url, method);
            if (requestCallback != null) {
                requestCallback.doWithRequest(request);
            }

            response = request.execute();
            handleResponse(url, method, response);
            return responseExtractor != null ? responseExtractor.extractData(response) : null;
        } catch (IOException ex) {
            String resource = url.toString();
            String query = url.getRawQuery();
            resource = (query != null ? resource.substring(0, resource.indexOf('?')) : resource);
            throw new ResourceAccessException("I/O error on " + method.name() + " request for \"" +
                    resource + "\": " + ex.getMessage(), ex);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * 把服务实例名称替换为 ip:port
     * @param url
     * @return
     */
    private URI replaceUrl(URI url) {
        // 解析微服务名称
        String sourceUrl = url.toString();
        String[] httpUrl = sourceUrl.split("//");
        int index = httpUrl[1].replaceFirst("/", "@").indexOf("@");
        String serviceName = httpUrl[1].substring(0, index);

        // 通过微服务的名称去 nacos 服务端获取对应的实例列表
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances(serviceName);
        if (CollectionUtils.isEmpty(serviceInstanceList)) {
            throw new RuntimeException("没有可用的微服务实例列表：" + serviceName);
        }

        // 采取随机获取一个
        Random random = new Random();
        int randomIndex = random.nextInt(serviceInstanceList.size());
        log.info("随机下标：{}", randomIndex);
        String serviceIp = serviceInstanceList.get(randomIndex).getUri().toString();
        log.info("随机选举的服务 ip：{}", serviceIp);
        String targetSource = httpUrl[1].replace(serviceName, serviceIp);

        try {
            return new URI(targetSource);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return url;
    }
}

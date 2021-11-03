package com.shura.mall.component;

import com.shura.mall.domain.MqCancelOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @Author: Garvey
 * @Created: 2021/11/3
 * @Description: 取消订单消息的发送者
 */
@Slf4j
@Component
public class CancelOrderSender implements InitializingBean {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendMessage(MqCancelOrder mqCancelOrder, final long delayTimes) {
        rocketMQTemplate.syncSend("mall.order.cancel.ttl", MessageBuilder.withPayload(mqCancelOrder).build(), delayTimes, 16);
        log.info("send cancel order message, orderId: {}", mqCancelOrder.getOrderId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}

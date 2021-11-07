package com.shura.mall.component;

import com.shura.mall.domain.OrderMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @Author: Garvey
 * @Created: 2021/11/3
 * @Description:
 */
@Slf4j
@Component
public class OrderMessageSender {

    @Value("${rocketmq.mall.scheduleTopic}")
    private String scheduleTopic;

    @Value("${rocketmq.mall.transGroup}")
    private String transGroup;

    @Value("${rocketmq.mall.transTopic}")
    private String transTopic;

    @Value("${rocketmq.mall.asyncOrderTopic}")
    private String asyncOrderTopic;

    private final String CANCEL_TAG = "cancelOrder";
    private final String TRANS_TAG = "trans";
    private final String ORDER_TAG = "create-order";

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送延时订单消息
     */
    public boolean sendTimeoutOrderMessage(String cancelId) {
        Message message = MessageBuilder.withPayload(cancelId)
                .setHeader(RocketMQHeaders.KEYS, cancelId)
                .build();
        SendResult result = rocketMQTemplate.syncSend(scheduleTopic + ":" + CANCEL_TAG, message, 5000, 15);
        return SendStatus.SEND_OK == result.getSendStatus();
    }

    /**
     * 事务消息，弱关联分布式系统柔性事务解决方案
     */
    public LocalTransactionState sendCartTransMessage() {
        Message message = MessageBuilder.withPayload("")
                .setHeader(RocketMQHeaders.KEYS, "")
                .build();
        TransactionSendResult result = rocketMQTemplate.sendMessageInTransaction(transGroup, transTopic + ":" + TRANS_TAG, message, null);
        log.info("事务消息-本地事务执行状态：{}，" + result.toString(), result.getLocalTransactionState().name());
        return result.getLocalTransactionState();
    }

    /**
     * 发送订单消息
     */
    public boolean sendCreateOrderMessage(OrderMessage orderMessage) {
        SendResult result = rocketMQTemplate.syncSend(asyncOrderTopic + ":" + ORDER_TAG, orderMessage);
        return SendStatus.SEND_OK == result.getSendStatus();
    }

    /**
     * 发送延迟同步库存消息，60s 后同步库存
     */
    public boolean sendStockSyncMessage(Long productId, Long promotionId) {
        Message message = MessageBuilder.withPayload(productId + ":" + promotionId).build();
        SendResult result = rocketMQTemplate.syncSend("stock-sync", message, 5000, 5);
        return SendStatus.SEND_OK == result.getSendStatus();
    }
}

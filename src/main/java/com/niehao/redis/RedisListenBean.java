package com.niehao.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

/**
 * ClassName: RedisListenBean
 * Package: com.niehao.redis
 * Description:
 *
 * @Author NieHao
 * @Create 2023/12/7 22:01
 * @Version 1.0
 */
@Component
public class RedisListenBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisListenBean.class);

    @Value("${server.port}")
    private String serverPort;

    @Value("${redis.channel.msgToAll}")
    private String msgToAll;

    @Value("${redis.channel.userStatus}")
    private String userStatus;

    /*
     * redis监听容器
     * */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // 监听msgToAll
        container.addMessageListener(listenerAdapter, new PatternTopic(msgToAll));
        container.addMessageListener(listenerAdapter, new PatternTopic(userStatus));
        LOGGER.info("Subscribed Redis channel: " + msgToAll);
        return container;
    }
}

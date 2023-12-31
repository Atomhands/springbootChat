package com.niehao.redis;

import com.niehao.model.ChatMessage;
import com.niehao.service.BootChatService;
import com.niehao.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

/**
 * ClassName: RedisListenHandle
 * Package: com.niehao.redis
 * Description:
 *
 * @Author NieHao
 * @Create 2023/12/7 22:01
 * @Version 1.0
 */
@Component
public class RedisListenHandle extends MessageListenerAdapter{
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisListenHandle.class);

    @Value("${redis.channel.msgToAll}")
    private String msgToAll;

    @Value("${redis.channel.userStatus}")
    private String userStatus;

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private BootChatService bootChatService;

    /*
    * redis  获取监听消息
    * */
    @Override
    public void onMessage(Message message, byte[] bytes) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();
        String rawMsg;
        String topic;
        try {
            rawMsg = redisTemplate.getStringSerializer().deserialize(body);
            topic = redisTemplate.getStringSerializer().deserialize(channel);
            LOGGER.info("Received raw message from topic:" + topic + ", raw message content：" + rawMsg);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return;
        }


        if (msgToAll.equals(topic)) {
            LOGGER.info("Send message to all users:" + rawMsg);
            ChatMessage chatMessage = JsonUtil.parseJsonToObj(rawMsg, ChatMessage.class);
            if (chatMessage != null) {
                bootChatService.sendMsg(chatMessage);
            }
        } else if (userStatus.equals(topic)) {
            ChatMessage chatMessage = JsonUtil.parseJsonToObj(rawMsg, ChatMessage.class);
            if (chatMessage != null) {
                bootChatService.alertUserStatus(chatMessage);
            }
        }else {
            LOGGER.warn("No further operation with this topic!");
        }
    }
}

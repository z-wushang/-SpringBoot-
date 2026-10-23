package com.kaoyan.system.module.message.service;

import com.kaoyan.system.module.message.entity.Notification;
import com.kaoyan.system.module.message.mapper.NotificationMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final NotificationMapper notificationMapper;

    public MessageService(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    public void push(Long userId, Long fromUserId, Integer type, String content, Long refId) {
        Notification n = new Notification();
        n.setUserId(userId);
        n.setFromUserId(fromUserId);
        n.setType(type);
        n.setContent(content);
        n.setRefId(refId);
        n.setIsRead(0);
        n.setCreateTime(LocalDateTime.now());
        notificationMapper.insert(n);
    }
}

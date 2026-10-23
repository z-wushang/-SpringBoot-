package com.kaoyan.system.module.message.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaoyan.system.common.result.Result;
import com.kaoyan.system.module.message.entity.Notification;
import com.kaoyan.system.module.message.mapper.NotificationMapper;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final NotificationMapper notificationMapper;

    public MessageController(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    @GetMapping
    public Result<List<Notification>> list(@RequestParam Long userId,
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "20") int size) {
        List<Notification> list = notificationMapper.selectList(
                new QueryWrapper<Notification>()
                        .eq("user_id", userId)
                        .orderByDesc("create_time")
                        .last("LIMIT " + size + " OFFSET " + (page - 1) * size)
        );
        return Result.success(list);
    }

    @GetMapping("/unread-count")
    public Result<Map<String, Object>> unreadCount(@RequestParam Long userId) {
        Long count = notificationMapper.selectCount(
                new QueryWrapper<Notification>()
                        .eq("user_id", userId)
                        .eq("is_read", 0)
        );
        Map<String, Object> result = new HashMap<>();
        result.put("count", count);
        return Result.success(result);
    }

    @PutMapping("/{id}/read")
    public Result<Void> markRead(@PathVariable Long id) {
        Notification n = notificationMapper.selectById(id);
        if (n != null) {
            n.setIsRead(1);
            notificationMapper.updateById(n);
        }
        return Result.success();
    }

    @PutMapping("/read-all")
    public Result<Void> readAll(@RequestParam Long userId) {
        Notification update = new Notification();
        update.setIsRead(1);
        notificationMapper.update(update,
                new QueryWrapper<Notification>()
                        .eq("user_id", userId)
                        .eq("is_read", 0)
        );
        return Result.success();
    }
}

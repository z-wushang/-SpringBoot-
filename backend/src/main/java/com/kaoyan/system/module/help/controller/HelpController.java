package com.kaoyan.system.module.help.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaoyan.system.common.result.Result;
import com.kaoyan.system.module.help.entity.HelpPost;
import com.kaoyan.system.module.help.mapper.HelpPostMapper;
import com.kaoyan.system.module.message.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/helps")
public class HelpController {

    private final HelpPostMapper helpMapper;
    private final MessageService messageService;

    public HelpController(HelpPostMapper helpMapper, MessageService messageService) {
        this.helpMapper = helpMapper;
        this.messageService = messageService;
    }

    @PostMapping
    public Result<Void> create(@RequestBody HelpPost help) {
        help.setStatus(1);
        help.setDeleted(0);
        help.setCreateTime(LocalDateTime.now());
        helpMapper.insert(help);
        if (help.getTargetUserId() != null) {
            messageService.push(help.getTargetUserId(), help.getUserId(), 2,
                    "收到新求助：" + help.getTitle(), help.getId());
        }
        return Result.success();
    }

    @GetMapping
    public Result<List<HelpPost>> list(@RequestParam(required = false) Long userId,
                                        @RequestParam(required = false) Long targetUserId) {
        QueryWrapper<HelpPost> qw = new QueryWrapper<>();
        qw.eq("deleted", 0);
        if (userId != null) qw.eq("user_id", userId);
        if (targetUserId != null) qw.eq("target_user_id", targetUserId);
        qw.orderByDesc("create_time");
        return Result.success(helpMapper.selectList(qw));
    }

    @PutMapping("/{id}/status")
    public Result<Void> close(@PathVariable Long id, @RequestParam Integer status) {
        HelpPost h = helpMapper.selectById(id);
        if (h != null) {
            h.setStatus(status);
            helpMapper.updateById(h);
            messageService.push(h.getUserId(), 0L, 3, "你的求助已更新状态", id);
        }
        return Result.success();
    }
}

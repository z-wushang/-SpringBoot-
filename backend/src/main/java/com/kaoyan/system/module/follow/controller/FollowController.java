package com.kaoyan.system.module.follow.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaoyan.system.common.result.Result;
import com.kaoyan.system.module.follow.entity.Follow;
import com.kaoyan.system.module.follow.mapper.FollowMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/follows")
public class FollowController {

    private final FollowMapper followMapper;

    public FollowController(FollowMapper followMapper) {
        this.followMapper = followMapper;
    }

    @PostMapping
    public Result<Void> follow(@RequestParam Long followerId, @RequestParam Long followedId) {
        Follow f = new Follow();
        f.setFollowerId(followerId);
        f.setFollowedId(followedId);
        f.setCreateTime(LocalDateTime.now());
        followMapper.insert(f);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> unfollow(@PathVariable Long id) {
        followMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping
    public Result<List<Follow>> list(@RequestParam Long followerId) {
        List<Follow> list = followMapper.selectList(
                new QueryWrapper<Follow>().eq("follower_id", followerId)
        );
        return Result.success(list);
    }
}

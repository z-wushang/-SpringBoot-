package com.kaoyan.system.module.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaoyan.system.common.result.Result;
import com.kaoyan.system.module.user.entity.User;
import com.kaoyan.system.module.user.mapper.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserMapper userMapper;

    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("/me")
    public Result<Map<String, Object>> me(@RequestParam Long userId) {
        User u = userMapper.selectOne(new QueryWrapper<User>().eq("id", userId));
        if (u == null) {
            return Result.error(404, "用户不存在");
        }
        Map<String, Object> vo = new HashMap<>();
        vo.put("id", u.getId());
        vo.put("username", u.getUsername());
        vo.put("nickname", u.getNickname());
        vo.put("email", u.getEmail());
        vo.put("examType", u.getExamType());
        vo.put("identity", u.getIdentity());
        return Result.success(vo);
    }
}

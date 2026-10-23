package com.kaoyan.system.module.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaoyan.system.common.result.Result;
import com.kaoyan.system.module.plan.entity.PlanTask;
import com.kaoyan.system.module.plan.mapper.PlanTaskMapper;
import com.kaoyan.system.module.study.entity.StudyRecord;
import com.kaoyan.system.module.study.mapper.StudyRecordMapper;
import com.kaoyan.system.module.user.entity.User;
import com.kaoyan.system.module.user.mapper.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserMapper userMapper;
    private final StudyRecordMapper studyMapper;
    private final PlanTaskMapper taskMapper;

    public AdminController(UserMapper userMapper, StudyRecordMapper studyMapper, PlanTaskMapper taskMapper) {
        this.userMapper = userMapper;
        this.studyMapper = studyMapper;
        this.taskMapper = taskMapper;
    }

    @GetMapping("/users")
    public Result<List<User>> users() {
        List<User> list = userMapper.selectList(
                new QueryWrapper<User>().eq("deleted", 0).orderByDesc("id")
        );
        list.forEach(u -> u.setPassword(null));
        return Result.success(list);
    }

    @GetMapping("/stats")
    public Result<Map<String, Object>> stats() {
        Map<String, Object> result = new HashMap<>();
        result.put("userCount", userMapper.selectCount(new QueryWrapper<User>().eq("deleted", 0)));
        result.put("taskCount", taskMapper.selectCount(new QueryWrapper<PlanTask>().eq("deleted", 0)));
        result.put("studyRecordCount", studyMapper.selectCount(null));
        return Result.success(result);
    }
}

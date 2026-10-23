package com.kaoyan.system.module.goal.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaoyan.system.common.result.Result;
import com.kaoyan.system.module.goal.entity.Goal;
import com.kaoyan.system.module.goal.mapper.GoalMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/goal")
public class GoalController {

    private final GoalMapper goalMapper;

    public GoalController(GoalMapper goalMapper) {
        this.goalMapper = goalMapper;
    }

    @GetMapping("/current")
    public Result<Map<String, Object>> current(@RequestParam Long userId) {
        Goal g = goalMapper.selectOne(
                new QueryWrapper<Goal>()
                        .eq("user_id", userId)
                        .eq("status", 1)
                        .eq("deleted", 0)
                        .orderByDesc("id")
                        .last("LIMIT 1")
        );
        Map<String, Object> result = new HashMap<>();
        if (g != null) {
            long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), g.getExamDate());
            result.put("goal", g);
            result.put("daysLeft", daysLeft);
        } else {
            result.put("goal", null);
            result.put("daysLeft", null);
        }
        return Result.success(result);
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody Goal goal) {
        goal.setStatus(1);
        goal.setDeleted(0);
        goal.setCreateTime(LocalDateTime.now());
        goalMapper.insert(goal);
        return Result.success();
    }
}

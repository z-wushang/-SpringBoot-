package com.kaoyan.system.module.plan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaoyan.system.common.result.Result;
import com.kaoyan.system.module.plan.entity.PlanTask;
import com.kaoyan.system.module.plan.mapper.PlanTaskMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final PlanTaskMapper taskMapper;

    public TaskController(PlanTaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @GetMapping("/today")
    public Result<List<PlanTask>> today(@RequestParam Long userId) {
        List<PlanTask> list = taskMapper.selectList(
                new QueryWrapper<PlanTask>()
                        .eq("user_id", userId)
                        .eq("plan_date", LocalDate.now())
                        .eq("deleted", 0)
                        .orderByAsc("priority")
        );
        return Result.success(list);
    }

    @PostMapping("/done/{id}")
    public Result<Void> done(@PathVariable Long id) {
        PlanTask t = taskMapper.selectById(id);
        if (t != null) {
            t.setStatus(1);
            taskMapper.updateById(t);
        }
        return Result.success();
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody PlanTask task) {
        task.setPlanDate(LocalDate.now());
        task.setStatus(0);
        task.setPriority(2);
        task.setDeleted(0);
        task.setCreateTime(LocalDateTime.now());
        taskMapper.insert(task);
        return Result.success();
    }
}

package com.kaoyan.system.module.study.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaoyan.system.common.result.Result;
import com.kaoyan.system.module.study.entity.StudyRecord;
import com.kaoyan.system.module.study.mapper.StudyRecordMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/study")
public class StudyRecordController {

    private final StudyRecordMapper studyMapper;

    public StudyRecordController(StudyRecordMapper studyMapper) {
        this.studyMapper = studyMapper;
    }

    @GetMapping("/week")
    public Result<Map<String, Object>> week(@RequestParam Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(6);
        List<StudyRecord> list = studyMapper.selectList(
                new QueryWrapper<StudyRecord>()
                        .eq("user_id", userId)
                        .ge("record_date", weekAgo)
        );
        Map<LocalDate, Integer> sumByDate = new HashMap<>();
        for (StudyRecord r : list) {
            sumByDate.merge(r.getRecordDate(), r.getDuration(), Integer::sum);
        }
        List<Map<String, Object>> trend = new ArrayList<>();
        int totalMinutes = 0;
        for (int i = 0; i < 7; i++) {
            LocalDate d = weekAgo.plusDays(i);
            int minutes = sumByDate.getOrDefault(d, 0);
            totalMinutes += minutes;
            Map<String, Object> day = new HashMap<>();
            day.put("date", d.toString());
            day.put("hours", Math.round(minutes / 60.0 * 10) / 10.0);
            trend.add(day);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("trend", trend);
        result.put("totalHours", Math.round(totalMinutes / 60.0 * 10) / 10.0);
        return Result.success(result);
    }
}

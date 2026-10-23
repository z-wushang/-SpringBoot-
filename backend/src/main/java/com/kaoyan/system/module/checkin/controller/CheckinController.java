package com.kaoyan.system.module.checkin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaoyan.system.common.result.Result;
import com.kaoyan.system.module.checkin.entity.CheckinRecord;
import com.kaoyan.system.module.checkin.mapper.CheckinRecordMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/checkin")
public class CheckinController {

    private final CheckinRecordMapper checkinMapper;

    public CheckinController(CheckinRecordMapper checkinMapper) {
        this.checkinMapper = checkinMapper;
    }

    @PostMapping("/do")
    public Result<Map<String, Object>> doCheckin(@RequestParam Long userId,
                                                  @RequestParam(required = false) Long taskId,
                                                  @RequestParam(required = false) Integer duration,
                                                  @RequestParam(required = false) String note) {
        CheckinRecord r = new CheckinRecord();
        r.setUserId(userId);
        r.setTaskId(taskId);
        r.setCheckinDate(LocalDate.now());
        r.setActualDuration(duration);
        r.setNote(note);
        r.setIsMakeup(0);
        checkinMapper.insert(r);

        Long streak = calcStreak(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("checkinId", r.getId());
        result.put("streak", streak);
        return Result.success(result);
    }

    @GetMapping("/streak")
    public Result<Long> streak(@RequestParam Long userId) {
        return Result.success(calcStreak(userId));
    }

    private Long calcStreak(Long userId) {
        LocalDate today = LocalDate.now();
        long streak = 0;
        LocalDate cursor = today;
        while (true) {
            Long count = checkinMapper.selectCount(
                    new QueryWrapper<CheckinRecord>()
                            .eq("user_id", userId)
                            .eq("checkin_date", cursor)
            );
            if (count != null && count > 0) {
                streak++;
                cursor = cursor.minusDays(1);
            } else {
                break;
            }
        }
        return streak;
    }
}

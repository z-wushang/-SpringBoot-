package com.kaoyan.system.controller;

import com.kaoyan.system.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Result<Map<String, String>> health() {
        return Result.success(Map.of(
                "status", "UP",
                "app", "kaoyan-system",
                "version", "1.0.0"
        ));
    }
}

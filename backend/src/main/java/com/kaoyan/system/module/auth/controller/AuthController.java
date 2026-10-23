package com.kaoyan.system.module.auth.controller;

import com.kaoyan.system.common.result.Result;
import com.kaoyan.system.module.auth.dto.LoginDTO;
import com.kaoyan.system.module.auth.dto.RegisterDTO;
import com.kaoyan.system.module.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public Result<Map<String, Object>> register(@Valid @RequestBody RegisterDTO dto) {
        return Result.success(authService.register(dto));
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDTO dto) {
        return Result.success(authService.login(dto));
    }

    @GetMapping("/code")
    public Result<Void> sendCode(@RequestParam String email) {
        authService.sendCode(email);
        return Result.success();
    }
}

package com.kaoyan.system.module.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaoyan.system.common.exception.BusinessException;
import com.kaoyan.system.common.utils.JwtUtils;
import com.kaoyan.system.module.auth.dto.LoginDTO;
import com.kaoyan.system.module.auth.dto.RegisterDTO;
import com.kaoyan.system.module.user.entity.User;
import com.kaoyan.system.module.user.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private static final String CODE_PREFIX = "auth:code:";

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final StringRedisTemplate redis;

    public AuthService(UserMapper userMapper,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils,
                       StringRedisTemplate redis) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.redis = redis;
    }

    public void sendCode(String email) {
        String code = String.format("%06d", new Random().nextInt(1000000));
        redis.opsForValue().set(CODE_PREFIX + email, code, 5, TimeUnit.MINUTES);
        log.info("【测试】邮箱 {} 的注册验证码是：{}（5 分钟内有效）", email, code);
    }

    public Map<String, Object> register(RegisterDTO dto) {
        String code = redis.opsForValue().get(CODE_PREFIX + dto.getEmail());
        if (code == null || !code.equals(dto.getCode())) {
            throw new BusinessException("验证码错误或已过期");
        }
        Long nameCount = userMapper.selectCount(new QueryWrapper<User>().eq("username", dto.getUsername()));
        if (nameCount != null && nameCount > 0) {
            throw new BusinessException("用户名已被占用");
        }
        Long emailCount = userMapper.selectCount(new QueryWrapper<User>().eq("email", dto.getEmail()));
        if (emailCount != null && emailCount > 0) {
            throw new BusinessException("邮箱已注册");
        }
        User u = new User();
        u.setUsername(dto.getUsername());
        u.setEmail(dto.getEmail());
        u.setNickname(dto.getUsername());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        u.setExamType(2);
        u.setIdentity(1);
        u.setRole(1);
        u.setStatus(1);
        u.setDeleted(0);
        userMapper.insert(u);
        redis.delete(CODE_PREFIX + dto.getEmail());
        return buildLoginResult(u);
    }

    public Map<String, Object> login(LoginDTO dto) {
        User u = userMapper.selectOne(new QueryWrapper<User>().eq("email", dto.getEmail()));
        if (u == null || !passwordEncoder.matches(dto.getPassword(), u.getPassword())) {
            throw new BusinessException("邮箱或密码错误");
        }
        if (u.getStatus() != null && u.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        return buildLoginResult(u);
    }

    private Map<String, Object> buildLoginResult(User u) {
        String role = u.getRole() == 0 ? "ADMIN" : (u.getIdentity() == 2 ? "MENTOR" : "STUDENT");
        String token = jwtUtils.generateToken(u.getId(), role);
        Map<String, Object> userVo = new HashMap<>();
        userVo.put("id", u.getId());
        userVo.put("username", u.getUsername());
        userVo.put("nickname", u.getNickname());
        userVo.put("email", u.getEmail());
        userVo.put("identity", u.getIdentity());
        userVo.put("role", role);
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", userVo);
        return result;
    }
}

package org.example.design.controller;

import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.design.common.R;
import org.example.design.domain.Change;
import org.example.design.domain.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;


import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/emailLogin")
@Component
public class emailController {
    private final RedisTemplate redisTemplate;
    private final JavaMailSenderImpl mailSender;
    private final userController userController;
    public emailController(RedisTemplate redisTemplate, JavaMailSenderImpl mailSender, userController userController) {
        this.redisTemplate = redisTemplate;
        this.mailSender = mailSender;
        this.userController = userController;
    }
    @PostMapping("/code")
    public R<String> sendEmail(@RequestBody String email) {
        boolean byPhone = userController.getByEmail(email);
        if(!byPhone)
        {
            return R.error("验证码发送失败");
        }
        JSONObject jsonObject = JSONObject.parseObject(email);
        String email1 = (String)jsonObject.get("email");
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * Math.pow(10, 5)));
        System.out.println(email1);
        redisTemplate.opsForValue().set(email1,code,120, TimeUnit.SECONDS);
        SimpleMailMessage simpMsg = new SimpleMailMessage();
        simpMsg.setFrom("2565929337@qq.com");
        simpMsg.setTo(email1);
        simpMsg.setSubject("code");
        simpMsg.setText(code);
        mailSender.send(simpMsg);
        return R.success("success");
    }
    @PostMapping("/equal")
    public  SaResult getEmail(@RequestBody String e) {
        JSONObject jsonObject = JSONObject.parseObject(e);
        String email = (String)jsonObject.get("email");
        String code = (String)jsonObject.get("code");
        System.out.println(email+code);
        String o = (String)redisTemplate.opsForValue().get(email);
        if(o.equals(code))
        {
            return SaResult.ok().setMsg("查询成功");
        }
        else {
            return SaResult.error().setMsg("查询失败");
        }
    }
    @GetMapping("login")
    public SaResult find(String email,String code)
    {
        String o = (String)redisTemplate.opsForValue().get(email);
        if(o.equals(code))
        {
            Change change = new Change();
            change.setMes("短信");
            change.setEmail(email);
            User s = userController.find(change);
            return SaResult.ok().setMsg("登录成功").setData(s);
        }
        else {
            return SaResult.error().setMsg("登录失败");
        }
    }
}
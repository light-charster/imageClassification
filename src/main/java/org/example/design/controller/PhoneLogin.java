package org.example.design.controller;


import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.design.common.R;
import org.example.design.domain.Change;
import org.example.design.domain.User;
import org.example.design.service.PhoneService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/Phone")
public class PhoneLogin {
    private final RedisTemplate<String, String> redisTemplate;
    private final userController userController;
    public PhoneLogin(RedisTemplate<String, String> redisTemplate, PhoneService phoneService, userController userController) {
        this.redisTemplate = redisTemplate;
        this.phoneService = phoneService;
        this.userController = userController;
    }
    private final PhoneService phoneService;
    @PostMapping("Code")
    public R<String> Login(@RequestBody String phone)
    {
        boolean byPhone = userController.getByPhone(phone);
        if(!byPhone)
        {
            return R.error("验证码发送失败");
        }
        JSONObject jsonObject = JSONObject.parseObject(phone);
        String phone1 = (String)jsonObject.get("phone");
        phoneService.send(phone1);
        return R.success("验证码发送成功");
    }
    @GetMapping("/Login")
    public SaResult Log(String code,String phone)
    {
        String sCode = redisTemplate.opsForValue().get(phone);
        if(code.equals(sCode))
        {
            Change change = new Change();
            change.setMes("电话");
            change.setPhone(phone);
            User user = userController.find(change);
            return SaResult.ok().setMsg("登陆成功").setData(user);
        }
        return SaResult.error().setMsg("登录失败");
    }

    @PostMapping("/equal")
    public SaResult equ(@RequestBody String c)
    {
        JSONObject jsonObject = JSONObject.parseObject(c);
        String phone = (String)jsonObject.get("phone");
        String code =(String)jsonObject.get("code");
        String sCode = redisTemplate.opsForValue().get(phone);
        if(code.equals(sCode))
        {
            return SaResult.ok().setMsg("查询成功");
        }
        return SaResult.error().setMsg("查询失败");
    }
}

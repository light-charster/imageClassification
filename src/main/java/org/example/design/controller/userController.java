package org.example.design.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.design.common.R;
import org.example.design.domain.Change;
import org.example.design.domain.User;
import org.example.design.service.impl.userserviceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/user")
public class userController {

    @Autowired
    private userserviceImpl userService;
    @PostMapping("/Login")    //登录功能
    public SaResult login(@RequestBody User user)
    {
        System.out.println(user);
        //1.将页面提交的密码进行md5加密处理
        String password=user.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        //根据页面提交的name查询数据库
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId,user.getId());
        User user1=userService.getOne(queryWrapper);
        //如果没有查到返回登录失败结果
        //System.out.println(password);
        if (user1==null)
        {
            return SaResult.error().setMsg("登录失败");
        }
        //密码比对
        if(!user1.getPassword().equals(password))
        {
            return SaResult.error().setMsg("密码错误");
        }
        //状态查询
        if(user1.getStatus()==0)
        {
            return SaResult.error().setMsg("账号封禁");
        }
        System.out.println(1);
        return SaResult.ok().setMsg("登陆成功").setData(user1);
    }

    @PostMapping("insert")    //新建用户功能，要求学号，学生真实姓名，学院，年级，密码不为空，其余不做硬性要求。
    public SaResult insert(@RequestBody User user)
    {
        user.setTime(LocalDateTime.now());
        user.setStatus(1);
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user.setName(user.getUsername());
        userService.save(user);
        return SaResult.ok().setMsg("添加成功");
    }

    @PostMapping("change")     //修改用信息功能
    public SaResult change(@RequestBody Change change)
    {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        User user = new User();
        user.setId(change.getId());
        queryWrapper.eq(User::getId,user.getId());
        User user1=userService.getOne(queryWrapper);
        String a=change.getMes();


        switch (a) {
            case "状态" -> user1.setStatus(change.getStatus());
            case "密码" -> user1.setPassword(DigestUtils.md5DigestAsHex(change.getPassword().getBytes()));
            case "电话" -> user1.setPhone(change.getPhone());
            case "邮箱" -> user1.setEmail(change.getEmail());
            case "用户名" -> user1.setUsername(change.getUsername());
            case "姓名" -> user1.setName(change.getName());
            case "学院" -> user1.setCollege(change.getCollege());
            case "年级" -> user1.setGrade(change.getGrade());
            case "性别" -> user1.setSex(change.getSex());
        }


        userService.updateById(user1);
        return SaResult.ok().setMsg("修改成功");
    }
    @PostMapping("delete")     //修改用信息功能
    public SaResult delete(@RequestBody User user)
    {
        userService.removeById(user.getId());
        return SaResult.ok().setMsg("删除成功");
    }
    @GetMapping("/page")
    public R<Page<User>> page(int page, int pageSize, String name)
    {
        Page<User> pageInfo= new Page<>(page, pageSize);
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name), User::getName,name);
        lambdaQueryWrapper.orderByDesc(User::getId);
        userService.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);
    }
    public String getIDPhone(String phone)
    {
        JSONObject jsonObject = JSONObject.parseObject(phone);
        String phone1 = (String)jsonObject.get("phone");
        System.out.println(phone1);
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,phone1);
        User user1=userService.getOne(queryWrapper);
        return user1.getId();
    }
    public boolean getByPhone(String phone)
    {
        JSONObject jsonObject = JSONObject.parseObject(phone);
        String phone1 = (String)jsonObject.get("phone");
        System.out.println(phone1);
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,phone1);
        User user1=userService.getOne(queryWrapper);
        if(user1!=null)
        {
            return user1.getStatus() == 1;
        }
        return false;
    }
    public boolean getByEmail(String email)
    {
        JSONObject jsonObject = JSONObject.parseObject(email);
        String email1 = (String)jsonObject.get("email");
        System.out.println(email1);
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email1);
        User user1=userService.getOne(queryWrapper);
        if(user1!=null)
        {
            return user1.getStatus() == 1;
        }
        return false;
    }
    public boolean getByID(String id)
    {
        JSONObject jsonObject = JSONObject.parseObject(id);
        String id1 = (String)jsonObject.get("id");
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId,id1);
        User user1=userService.getOne(queryWrapper);
        if(user1!=null)
        {
            return user1.getStatus() == 1;
        }
        return false;
    }
    public String getIDEmail(String email)
    {
        JSONObject jsonObject = JSONObject.parseObject(email);
        String email1 = (String)jsonObject.get("email");
        System.out.println(email1);
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email1);
        User user1=userService.getOne(queryWrapper);
        return user1.getId();
    }
    public String getIDID(String id)
    {
        JSONObject jsonObject = JSONObject.parseObject(id);
        String id1 = (String)jsonObject.get("id");
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId,id1);
        User user1=userService.getOne(queryWrapper);
        return user1.getId();
    }
    public User find(Change change)
    {
        String mes = change.getMes();
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        System.out.println(change);
        System.out.println(mes);
        switch (mes) {
            case "电话":
                String phone = change.getPhone();
                queryWrapper.eq(User::getPhone, phone);
                userService.getOne(queryWrapper);
                break;
            case "短信":
                String email = change.getEmail();
                queryWrapper.eq(User::getEmail, email);
                userService.getOne(queryWrapper);
                break;
            case "微信":
                String username = change.getUsername();
                queryWrapper.eq(User::getUsername, username);
                userService.getOne(queryWrapper);
                break;
        }
        return userService.getOne(queryWrapper);
    }
}

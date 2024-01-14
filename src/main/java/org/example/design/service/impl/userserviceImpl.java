package org.example.design.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.design.domain.User;
import org.example.design.mapper.userMapper;
import org.example.design.service.userService;
import org.springframework.stereotype.Service;


@Service
public class userserviceImpl extends ServiceImpl<userMapper, User> implements userService {

}

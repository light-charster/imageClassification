package org.example.design.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.design.domain.User;


@Mapper
public interface userMapper extends BaseMapper<User> {
}

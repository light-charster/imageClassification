package org.example.design.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.design.domain.Relic;
import org.example.design.mapper.RelicMapper;
import org.example.design.service.RelicService;
import org.springframework.stereotype.Service;


@Service
public class RelicServiceImpl extends ServiceImpl<RelicMapper, Relic> implements RelicService{

}

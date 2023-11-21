package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.auth.data;
import com.example.mapper.DataMapper;
import com.example.service.DataService;
import org.springframework.stereotype.Service;

@Service
public class DataServiceImpl extends ServiceImpl<DataMapper, data> implements DataService {
}

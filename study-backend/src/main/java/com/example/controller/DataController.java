package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.entity.auth.data;
import com.example.service.DataService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private DataService dataService;

    @GetMapping("/list")
            public List<data> list(){ return dataService.list(); }

    @PostMapping("/listP")
    public List<data> listP(@RequestBody data data1){
        LambdaQueryWrapper<data> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(data::getName, data1.getName());
        return dataService.list(lambdaQueryWrapper);
    }
}

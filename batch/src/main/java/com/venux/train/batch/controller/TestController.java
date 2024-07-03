package com.venux.train.batch.controller;


import com.venux.train.batch.feign.BusinessFeign;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    @Resource
    BusinessFeign businessFeign;

    @GetMapping("/hello")
    public String hello(){
        String businessHello = businessFeign.hello();
        LOG.info("businessHello: {}", businessHello);
        return "hello world from batch!";
    }
}

package com.venux.train.member.controller;


import com.venux.train.common.resp.CommonResp;
import com.venux.train.member.req.PassengerSaveReq;
import com.venux.train.member.service.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passenger")
public class PassengerController {
    @Resource
    private PassengerService passengerService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody PassengerSaveReq req){

        passengerService.save(req);
        return new CommonResp<>();
    }

}

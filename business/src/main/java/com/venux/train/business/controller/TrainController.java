package com.venux.train.business.controller;


import com.venux.train.business.req.TrainQueryReq;
import com.venux.train.business.req.TrainSaveReq;
import com.venux.train.business.resp.TrainQueryResp;
import com.venux.train.business.service.TrainSeatService;
import com.venux.train.business.service.TrainService;
import com.venux.train.common.resp.CommonResp;
import com.venux.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/train")
public class TrainController {
    @Resource
    private TrainService trainService;

    @GetMapping("/query-all")
    public CommonResp<List<TrainQueryResp>> queryList(){
        List<TrainQueryResp> list = trainService.queryAll();
        return new CommonResp<>(list);
    }

}

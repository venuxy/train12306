package com.venux.train.business.controller;


import com.venux.train.business.req.StationQueryReq;
import com.venux.train.business.req.StationSaveReq;
import com.venux.train.business.resp.StationQueryResp;
import com.venux.train.business.service.StationService;
import com.venux.train.common.resp.CommonResp;
import com.venux.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/station")
public class StationController {
    @Resource
    private StationService stationService;

    @GetMapping("/query-all")
    public CommonResp<List<StationQueryResp>> queryList(){
        List<StationQueryResp> list = stationService.queryAll();
        return new CommonResp<>(list);
    }

}

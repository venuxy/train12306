package com.venux.train.business.controller.admin;


import com.venux.train.common.context.LoginMemberContext;
import com.venux.train.common.resp.CommonResp;
import com.venux.train.common.resp.PageResp;
import com.venux.train.business.req.DailyTrainTicketQueryReq;
import com.venux.train.business.req.DailyTrainTicketSaveReq;
import com.venux.train.business.resp.DailyTrainTicketQueryResp;
import com.venux.train.business.service.DailyTrainTicketService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/daily-train-ticket")
public class DailyTrainTicketAdminController {
    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody DailyTrainTicketSaveReq req) {
        dailyTrainTicketService.save(req);
        return new CommonResp<>();
    }
    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainTicketQueryResp>> queryList(@Valid DailyTrainTicketQueryReq req){
        PageResp<DailyTrainTicketQueryResp> list = dailyTrainTicketService.queryList(req);
        return new CommonResp<>(list);
    }

    @GetMapping("/query-list2")
    public CommonResp<PageResp<DailyTrainTicketQueryResp>> queryList2(@Valid DailyTrainTicketQueryReq req){
        PageResp<DailyTrainTicketQueryResp> list = dailyTrainTicketService.queryList2(req);
        return new CommonResp<>(list);
    }
    @GetMapping("/query-list3")
    public CommonResp<PageResp<DailyTrainTicketQueryResp>> queryList3(@Valid DailyTrainTicketQueryReq req){
        PageResp<DailyTrainTicketQueryResp> list = dailyTrainTicketService.queryList3(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        dailyTrainTicketService.delete(id);
        return new CommonResp<>();
    }

}

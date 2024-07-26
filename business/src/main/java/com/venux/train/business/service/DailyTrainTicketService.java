package com.venux.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.venux.train.business.domain.DailyTrainStation;
import com.venux.train.business.domain.TrainStation;
import com.venux.train.common.resp.PageResp;
import com.venux.train.common.util.SnowUtil;
import com.venux.train.business.domain.DailyTrainTicket;
import com.venux.train.business.domain.DailyTrainTicketExample;
import com.venux.train.business.mapper.DailyTrainTicketMapper;
import com.venux.train.business.req.DailyTrainTicketQueryReq;
import com.venux.train.business.req.DailyTrainTicketSaveReq;
import com.venux.train.business.resp.DailyTrainTicketQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class DailyTrainTicketService {
    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainTicketService.class);
    @Resource
    private DailyTrainTicketMapper dailyTrainTicketMapper;

    @Resource
    private TrainStationService trainStationService;


    public void save(DailyTrainTicketSaveReq req){
        DateTime now = DateTime.now();
        DailyTrainTicket dailyTrainTicket = BeanUtil.copyProperties(req, DailyTrainTicket.class);
        //如果id为空，说明是新增，否则是修改
        if (ObjectUtil.isNull(dailyTrainTicket.getId())) {
            dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainTicket.setCreateTime(now);
            dailyTrainTicket.setUpdateTime(now);
            dailyTrainTicketMapper.insert(dailyTrainTicket);
        } else {
            dailyTrainTicket.setUpdateTime(now);
            dailyTrainTicketMapper.updateByPrimaryKey(dailyTrainTicket);
        }
    }
    public PageResp<DailyTrainTicketQueryResp> queryList(DailyTrainTicketQueryReq req){
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.setOrderByClause("id desc");
        DailyTrainTicketExample.Criteria criteria = dailyTrainTicketExample.createCriteria();


        LOG.info("查询页码，{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());

        PageHelper.startPage(req.getPage(), req.getSize());
        List<DailyTrainTicket> dailyTrainTicketList = dailyTrainTicketMapper.selectByExample(dailyTrainTicketExample);

        PageInfo<DailyTrainTicket> pageInfo = new PageInfo(dailyTrainTicketList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<DailyTrainTicketQueryResp> list = BeanUtil.copyToList(dailyTrainTicketList, DailyTrainTicketQueryResp.class);
        PageResp<DailyTrainTicketQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        dailyTrainTicketMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    public void genDaily(Date date, String trainCode) {
        LOG.info("生成日期【{}】车次【{}】的余票信息开始", DateUtil.formatDate(date), trainCode);

        // 删除某日某车次的余票信息
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode);
        dailyTrainTicketMapper.deleteByExample(dailyTrainTicketExample);

        // 查询此车次的所有站
        List<TrainStation> stationList = trainStationService.selectByTrainCode(trainCode);
        if (CollUtil.isEmpty(stationList)) {
            LOG.info("该车次没有车站基础数据，生成该车次的车站余票结束");
            return;
        }

        DateTime now = DateTime.now();
        for (int i = 0; i < stationList.size(); i++) {
            //得到出发站
            TrainStation fromStation = stationList.get(i);
            for (int j = i + 1; j < stationList.size(); j++) {
                TrainStation toStation = stationList.get(j);

                DailyTrainTicket dailyTrainTicket = new DailyTrainTicket();
                dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
                dailyTrainTicket.setDate(date);
                dailyTrainTicket.setTrainCode(trainCode);
                dailyTrainTicket.setStart(fromStation.getName());
                dailyTrainTicket.setStartPinyin(fromStation.getNamePinyin());
                dailyTrainTicket.setStartTime(fromStation.getOutTime());
                dailyTrainTicket.setStartIndex(fromStation.getIndex());
                dailyTrainTicket.setEnd(toStation.getName());
                dailyTrainTicket.setEndPinyin(toStation.getNamePinyin());
                dailyTrainTicket.setEndTime(toStation.getInTime());
                dailyTrainTicket.setEndIndex(toStation.getIndex());
                dailyTrainTicket.setYdz(0);
                dailyTrainTicket.setYdzPrice(BigDecimal.ZERO);
                dailyTrainTicket.setEdz(0);
                dailyTrainTicket.setEdzPrice(BigDecimal.ZERO);
                dailyTrainTicket.setRw(0);
                dailyTrainTicket.setRwPrice(BigDecimal.ZERO);
                dailyTrainTicket.setYw(0);
                dailyTrainTicket.setYwPrice(BigDecimal.ZERO);
                dailyTrainTicket.setCreateTime(now);
                dailyTrainTicket.setUpdateTime(now);
                dailyTrainTicketMapper.insert(dailyTrainTicket);
            }
        }

        LOG.info("生成日期【{}】车次【{}】的车站信息结束", DateUtil.formatDate(date), trainCode);
    }
}

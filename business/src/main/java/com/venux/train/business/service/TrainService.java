package com.venux.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.venux.train.common.resp.PageResp;
import com.venux.train.common.util.SnowUtil;
import com.venux.train.business.domain.Train;
import com.venux.train.business.domain.TrainExample;
import com.venux.train.business.mapper.TrainMapper;
import com.venux.train.business.req.TrainQueryReq;
import com.venux.train.business.req.TrainSaveReq;
import com.venux.train.business.resp.TrainQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainService {
    private static final Logger LOG = LoggerFactory.getLogger(TrainService.class);
    @Resource
    private TrainMapper trainMapper;

    public void save(TrainSaveReq req){
        DateTime now = DateTime.now();
        Train train = BeanUtil.copyProperties(req, Train.class);
        //如果id为空，说明是新增，否则是修改
        if (ObjectUtil.isNull(train.getId())) {
            train.setId(SnowUtil.getSnowflakeNextId());
            train.setCreateTime(now);
            train.setUpdateTime(now);
            trainMapper.insert(train);
        } else {
            train.setUpdateTime(now);
            trainMapper.updateByPrimaryKey(train);
        }
    }
    public PageResp<TrainQueryResp> queryList(TrainQueryReq req){
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("create_time desc");
        TrainExample.Criteria criteria = trainExample.createCriteria();


        LOG.info("查询页码，{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());

        PageHelper.startPage(req.getPage(), req.getSize());
        List<Train> trainList = trainMapper.selectByExample(trainExample);

        PageInfo<Train> pageInfo = new PageInfo(trainList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<TrainQueryResp> list = BeanUtil.copyToList(trainList, TrainQueryResp.class);
        PageResp<TrainQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        trainMapper.deleteByPrimaryKey(id);
    }
}

package com.venux.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.venux.train.business.mapper.cust.SkTokenMapperCust;
import com.venux.train.common.resp.PageResp;
import com.venux.train.common.util.SnowUtil;
import com.venux.train.business.domain.SkToken;
import com.venux.train.business.domain.SkTokenExample;
import com.venux.train.business.mapper.SkTokenMapper;
import com.venux.train.business.req.SkTokenQueryReq;
import com.venux.train.business.req.SkTokenSaveReq;
import com.venux.train.business.resp.SkTokenQueryResp;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SkTokenService {
    private static final Logger LOG = LoggerFactory.getLogger(SkTokenService.class);
    @Resource
    private SkTokenMapper skTokenMapper;

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @Resource
    private SkTokenMapperCust skTokenMapperCust;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 初始化
     */
    public void genDaily(Date date, String trainCode) {
        LOG.info("删除日期【{}】车次【{}】的令牌记录", DateUtil.formatDate(date), trainCode);
        SkTokenExample skTokenExample = new SkTokenExample();
        skTokenExample.createCriteria().andDateEqualTo(date).andTrainCodeEqualTo(trainCode);
        skTokenMapper.deleteByExample(skTokenExample);

        DateTime now = DateTime.now();
        SkToken skToken = new SkToken();
        skToken.setDate(date);
        skToken.setTrainCode(trainCode);
        skToken.setId(SnowUtil.getSnowflakeNextId());
        skToken.setCreateTime(now);
        skToken.setUpdateTime(now);

        int seatCount = dailyTrainSeatService.countSeat(date, trainCode);
        LOG.info("车次【{}】座位数：{}", trainCode, seatCount);

        long stationCount = dailyTrainStationService.countByTrainCode(date, trainCode);
        LOG.info("车次【{}】到站数：{}", trainCode, stationCount);

        // 3/4需要根据实际卖票比例来定，一趟火车最多可以卖（seatCount * stationCount）张火车票
        int count = (int) (seatCount * stationCount); // * 3/4);
        LOG.info("车次【{}】初始生成令牌数：{}", trainCode, count);
        skToken.setCount(count);

        skTokenMapper.insert(skToken);
    }
    public void save(SkTokenSaveReq req){
        DateTime now = DateTime.now();
        SkToken skToken = BeanUtil.copyProperties(req, SkToken.class);
        //如果id为空，说明是新增，否则是修改
        if (ObjectUtil.isNull(skToken.getId())) {
            skToken.setId(SnowUtil.getSnowflakeNextId());
            skToken.setCreateTime(now);
            skToken.setUpdateTime(now);
            skTokenMapper.insert(skToken);
        } else {
            skToken.setUpdateTime(now);
            skTokenMapper.updateByPrimaryKey(skToken);
        }
    }
    public PageResp<SkTokenQueryResp> queryList(SkTokenQueryReq req){
        SkTokenExample skTokenExample = new SkTokenExample();
        skTokenExample.setOrderByClause("id desc");
        SkTokenExample.Criteria criteria = skTokenExample.createCriteria();


        LOG.info("查询页码，{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());

        PageHelper.startPage(req.getPage(), req.getSize());
        List<SkToken> skTokenList = skTokenMapper.selectByExample(skTokenExample);

        PageInfo<SkToken> pageInfo = new PageInfo(skTokenList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<SkTokenQueryResp> list = BeanUtil.copyToList(skTokenList, SkTokenQueryResp.class);
        PageResp<SkTokenQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        skTokenMapper.deleteByPrimaryKey(id);
    }

    public boolean validSkToken(Date date, String trainCode, Long memberId) {
         LOG.info("会员【{}】获取日期【{}】车次【{}】的令牌开始", memberId, DateUtil.formatDate(date), trainCode);
//         令牌约等于库存，令牌没有了，就不再卖票，不需要再进入购票主流程去判断库存，判断令牌肯定比判断库存效率高
         String lockKey = DateUtil.formatDate(date) + "-" + trainCode + "-" + memberId;
//         RLock lock = redissonClient.getLock(lockKey);
//         Boolean setIfAbsent = lock.tryLock(3, 10, TimeUnit.SECONDS);
         Boolean setIfAbsent = redisTemplate.opsForValue().setIfAbsent(lockKey, lockKey, 5, TimeUnit.SECONDS);
         if (Boolean.TRUE.equals(setIfAbsent)) {
             LOG.info("恭喜，抢到令牌锁了！lockKey：{}", lockKey);
         } else {
             LOG.info("很遗憾，没抢到令牌锁！lockKey：{}", lockKey);
             return false;
         }

         int updateCount = skTokenMapperCust.decrease(date, trainCode, 1);
         if (updateCount > 0) {
             return true;
         } else {
             return false;
         }
    }
}

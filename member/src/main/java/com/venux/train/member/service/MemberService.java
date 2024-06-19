package com.venux.train.member.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.venux.train.common.exception.BusinessException;
import com.venux.train.common.exception.BusinessExceptionEnum;
import com.venux.train.common.util.SnowUtil;
import com.venux.train.member.domain.Member;
import com.venux.train.member.domain.MemberExample;
import com.venux.train.member.mapper.MemberMapper;
import com.venux.train.member.req.MemberRegisterReq;
import com.venux.train.member.req.MemberSendCodeReq;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
        private static final Logger LOG = LoggerFactory.getLogger(MemberService.class);
        @Resource
        private MemberMapper memberMapper;
        public int count() {
            return Math.toIntExact(memberMapper.countByExample(null));
        }

        public long register(MemberRegisterReq req) {
                String mobile = req.getMobile();

                MemberExample memberExample = new MemberExample();

                memberExample.createCriteria().andMobileEqualTo(mobile);

                List<Member> list = memberMapper.selectByExample(memberExample);

                if (CollUtil.isNotEmpty(list)) {
                    //return list.get(0).getId();
                        throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
                }

                Member member = new Member();
                member.setId(SnowUtil.getSnowflakeNextId());
                member.setMobile(mobile);
                memberMapper.insert(member);
                return member.getId();
        }

        public void sendCode(MemberSendCodeReq req) {
                String mobile = req.getMobile();

                MemberExample memberExample = new MemberExample();

                memberExample.createCriteria().andMobileEqualTo(mobile);

                List<Member> list = memberMapper.selectByExample(memberExample);

                //如果手机号不存在 插入一条记录
                if (CollUtil.isEmpty(list)) {
                        LOG.info("手机号不存在，插入一条记录");
                        Member member = new Member();
                        member.setId(SnowUtil.getSnowflakeNextId());
                        member.setMobile(mobile);
                        memberMapper.insert(member);
                } else {
                        LOG.info("手机号存在，无需插入新记录");
                }

                //生成验证码
                //String code = RandomUtil.randomString("0123456789", 4);
                String  code = "8888";

                LOG.info("发送短信验证码：{}", code);

                //保存短信记录表：手机号，短信验证码，有效期，是否已使用，业务类型，发送时间，使用时间

                LOG.info("保存短信记录表：{}", code);

                //对接短信通道，发送短信

                LOG.info("对接短信通道，发送短信");
        }
}

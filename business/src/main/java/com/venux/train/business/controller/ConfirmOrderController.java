package com.venux.train.business.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.venux.train.business.req.ConfirmOrderDoReq;
import com.venux.train.business.service.BeforeConfirmOrderService;
import com.venux.train.business.service.ConfirmOrderService;
import com.venux.train.common.exception.BusinessExceptionEnum;
import com.venux.train.common.resp.CommonResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/confirm-order")
public class ConfirmOrderController {
    private static final Logger LOG = LoggerFactory.getLogger(ConfirmOrderController.class);
    @Resource
    private BeforeConfirmOrderService beforeConfirmOrderService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${spring.profiles.active}")
    private String env;

    @SentinelResource(value = "confirmOrderDo", blockHandler = "doConfirmBlock")
    @PostMapping("/do")
    public CommonResp<Object> doConfirm(@Valid @RequestBody ConfirmOrderDoReq req) {
        if (!env.equals("dev")) {
//             图形验证码校验
            String imageCodeToken = req.getImageCodeToken();
            String imageCode = req.getImageCode();
            String imageCodeRedis = stringRedisTemplate.opsForValue().get(imageCodeToken);
            LOG.info("从redis中获取到的验证码：{}", imageCodeRedis);
            if (ObjectUtils.isEmpty(imageCodeRedis)) {
                return new CommonResp<>(false, "验证码已过期", null);
            }
            // 验证码校验，大小写忽略，提升体验，比如Oo Vv Ww容易混
            if (!imageCodeRedis.equalsIgnoreCase(imageCode)) {
                return new CommonResp<>(false, "验证码不正确", null);
            } else {
                // 验证通过后，移除验证码
                stringRedisTemplate.delete(imageCodeToken);
            }
            beforeConfirmOrderService.beforeDoConfirm(req);
            return new CommonResp<>();

        }
        LOG.info("hhh");
        Long id = beforeConfirmOrderService.beforeDoConfirm(req);
        return new CommonResp<>(String.valueOf(id));
    }

    public CommonResp<Object> doConfirmBlock(ConfirmOrderDoReq req, BlockException e) {
        LOG.info("ConfirmOrderController购票请求被限流：{}", req);
        // throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_FLOW_EXCEPTION);
        CommonResp<Object> commonResp = new CommonResp<>();
        commonResp.setSuccess(false);
        commonResp.setMessage(BusinessExceptionEnum.CONFIRM_ORDER_FLOW_EXCEPTION.getDesc());
        return commonResp;

    }
}

package com.frank.coupon.feign.hystrix;

import com.frank.coupon.feign.TemplateClient;
import com.frank.coupon.vo.CommonResponse;
import com.frank.vo.CouponTemplateSDK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Hystrix要求与feign client接口定义的名称和参数都一样
 */

@Component
public class TemplateClientHystrix implements TemplateClient{
    private Logger logger = LoggerFactory.getLogger(TemplateClientHystrix.class);

    @Override
    public CommonResponse<List<CouponTemplateSDK>> findAllUsableTemplates() {
        logger.error("[eureka-client-coupon-template] findAllUsableTemplates request failed");
        return new CommonResponse<>(-1,
                "[eureka-client-coupon-template] findAllUsableTemplates request failed",
                Collections.emptyList());
    }

    @Override
    public CommonResponse<List<CouponTemplateSDK>> findByIds(Collection<Integer> ids) {
        logger.error("[eureka-client-coupon-template] findByIds request failed");
        return new CommonResponse<>(-1,
                "[eureka-client-coupon-template] findByIds request failed",
                Collections.emptyList());
    }
}

package com.frank.coupon.feign.hystrix;

import com.frank.coupon.feign.SettlementClient;
import com.frank.coupon.vo.CommonResponse;
import com.frank.coupon.vo.SettlementInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SettlementClientHystrix implements SettlementClient {
    private Logger logger = LoggerFactory.getLogger(SettlementClientHystrix.class);

    @Override
    public CommonResponse<SettlementInfo> computeRule(SettlementInfo settlementInfo) {

        logger.error("[eureka-client-coupon-settlement] computeRule request failed");

        settlementInfo.setComputed(false);
        settlementInfo.setPrice(-1.0);
        return new CommonResponse<SettlementInfo>(-1,
                "[eureka-client-coupon-settlement] computeRule request failed",
                settlementInfo);
    }
}

package com.frank.coupon.feign;

import com.frank.coupon.feign.hystrix.TemplateClientHystrix;
import com.frank.coupon.vo.CommonResponse;
import com.frank.vo.CouponTemplateSDK;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

@FeignClient(value = "eureka-client-coupon-template", fallback = TemplateClientHystrix.class)
public interface TemplateClient {

    //mapping要与controller中的一样
    @RequestMapping(value = "/coupon-template/all", method = RequestMethod.GET)
    CommonResponse<List<CouponTemplateSDK>> findAllUsableTemplates();

    @RequestMapping(value = "/coupon-template/search", method = RequestMethod.GET)
    CommonResponse<List<CouponTemplateSDK>> findByIds(@RequestParam("ids") Collection<Integer> ids);
}

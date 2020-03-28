package com.frank.controller;


import com.alibaba.fastjson.JSON;
import com.frank.coupon.exception.CouponException;
import com.frank.entity.CouponTemplate;
import com.frank.service.CouponTemplateService;
import com.frank.vo.CouponTemplateRequest;
import com.frank.vo.CouponTemplateSDK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/coupon-template")
public class CouponTemplateController {
    private Logger logger = LoggerFactory.getLogger(CouponTemplateController.class);

    @Autowired
    private CouponTemplateService couponTemplateService;

    /**
     * localhost:7001/coupon-template/id             直接访问
     * localhost:9000/frank-zuul/coupon-template/id  通过网关转发访问
     * @param id
     * @return
     * @throws CouponException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public CouponTemplate get(@PathVariable(value = "id") Integer id) throws CouponException {
        return couponTemplateService.findById(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "")
    public CouponTemplate create(@RequestBody CouponTemplateRequest request) throws CouponException {
        logger.info("create template:{}", JSON.toJSONString(request));
        return couponTemplateService.create(request);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<CouponTemplateSDK> findAllUsable() throws CouponException {
        return couponTemplateService.findAllUsable();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public List<CouponTemplateSDK> findByIds(@RequestParam("ids") Collection<Integer> ids) throws CouponException {
        return couponTemplateService.findByIds(ids);
    }
}

package com.frank.service.impl;

import com.frank.coupon.constant.Constant;
import com.frank.dao.CouponTemplateDao;
import com.frank.entity.CouponTemplate;
import com.frank.service.AsyncService;
import com.google.common.base.Stopwatch;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class AsyncServiceImpl implements AsyncService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncServiceImpl.class);

    @Autowired
    private CouponTemplateDao couponTemplateDao;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    @Async("getAsyncExecutor")
    public void asyncCreateCouponByTemplate(CouponTemplate template) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        Set<String> couponCodes = buildCouponCodeSet(template);

        String redisKey = String.format("%s%s", Constant.RedisPrefix.COUPON_TEMPLATE, template.getId().toString());
        redisTemplate.opsForList().rightPushAll(redisKey, couponCodes);

//        logger.info("push CouponCode to Reids", stopwatch.elapsed(TimeUnit.MICROSECONDS));

        template.setAvailable(true);
        couponTemplateDao.save(template);

        stopwatch.stop();
        logger.info("async create coupon code coast: {} ms", stopwatch.elapsed(TimeUnit.MICROSECONDS));

        //todo 发送短信或者邮件通知优惠券模板可用
        logger.info("CouponCode({}) is availabe!", template.getId());
    }

    /**
     * 18位
     * 前4位： 产品线+类型
     * 中间6位 日期随机(20200202)
     * 后八位 0 -9随机
     * set的返回个数和CouponTemplate.count相同
     * @param template
     * @return
     */
    private Set<String> buildCouponCodeSet(CouponTemplate template){
        Stopwatch stopwatch = Stopwatch.createStarted();

        Set<String> result = new HashSet<>(template.getCount());
        for (int i =0; i<template.getCount(); i++){
            result.add(buildCouponCode(template));
        }
        while (result.size() < template.getCount()){
            result.add(buildCouponCode(template));
        }
        stopwatch.stop();

        logger.info("build coupon code coast: {} ms", stopwatch.elapsed(TimeUnit.MICROSECONDS));
        return result;
    }

    private String buildCouponCode(CouponTemplate template){
        char[] bases = new char[]{'1','2','3','4','5','6','7','8','9'};
        String suffix8 = RandomStringUtils.random(1, bases)
                + RandomStringUtils.randomAlphabetic(7);

        return template.getProductLine().getCode().toString() + template.getCategory().getCode()
                + new SimpleDateFormat("yyMMdd").format(template.getCreateTime())
                + suffix8;
    }
//    private String shuffleDate(String date){
//        date.chars().mapto
//    }
}

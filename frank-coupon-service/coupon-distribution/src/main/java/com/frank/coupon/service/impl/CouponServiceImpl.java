package com.frank.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.frank.coupon.constant.Constant;
import com.frank.coupon.constant.CouponStatus;
import com.frank.coupon.dao.CouponDao;
import com.frank.coupon.entity.Coupon;
import com.frank.coupon.exception.CouponException;
import com.frank.coupon.feign.SettlementClient;
import com.frank.coupon.feign.TemplateClient;
import com.frank.coupon.service.CouponService;
import com.frank.coupon.service.KafkaService;
import com.frank.coupon.service.RedisService;
import com.frank.coupon.vo.AcquireTemplateRequest;
import com.frank.coupon.vo.CouponClassify;
import com.frank.coupon.vo.CouponKafkaMsg;
import com.frank.coupon.vo.SettlementInfo;
import com.frank.vo.CouponTemplateSDK;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 所有的操作过程，状态都报错在reids中，并通过kafka把消息传递到mysql中
 * 注意：直接使用springboot中的异步任务，异步任务有可能失败。kafka失败后还可以从消息队列中重新取，保证安全性
 */
@Service
public class CouponServiceImpl implements CouponService {
    private Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private RedisService redisService;

    @Autowired
    private KafkaService kafkaService;

    //kafka客户端
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    //模板微服务客户端
    @Autowired
    private TemplateClient templateClient;

    //结算微服务客户端
    @Autowired
    private SettlementClient settlementClient;

    @Override
    public List<Coupon> findByUserIdStatus(Long userId, Integer status) throws CouponException {
        List<Coupon> couponList = redisService.getCachedCoupons(userId, status);
        List<Coupon> targetList;
        if(!CollectionUtils.isEmpty(couponList)){
            logger.debug("coupon cache is exist, get from db: {}, {}", userId, status);
            //将无效优惠券剔除
            targetList = couponList.stream().filter(item-> item.getId()!=-1).collect(Collectors.toList());
        }else{
            logger.debug("coupon cache is empty, get from db: {}, {}", userId, status);
            List<Coupon> dbCouponList = couponDao.findAllByUserIdAndStatus(userId, CouponStatus.of(status));

            if(CollectionUtils.isEmpty(dbCouponList)){
                logger.debug("current user not have coupon: {}, {}", userId, status);
                return dbCouponList;
            }
            //填充CouponTemplateSDK
            List<Integer> templateIds = dbCouponList.stream().map(Coupon::getTemplateId).collect(Collectors.toList());
            List<CouponTemplateSDK> couponTemplateSDKList = templateClient.findByIds(templateIds).getData();
            for (Coupon coupon: dbCouponList){
                CouponTemplateSDK sdk = couponTemplateSDKList.stream().filter(item->item.getId().equals(coupon.getTemplateId())).findAny().get();
                coupon.setCouponTemplateSDK(sdk);
            }
            targetList = dbCouponList;
            //将记录写入cache
            redisService.addCouponsToCache(userId, dbCouponList, status);
        }
        //如果获取的是可用的优惠券，还需要对过期优惠券的延时处理
        if(CouponStatus.of(status) == CouponStatus.USABLE){
            CouponClassify couponClassify = CouponClassify.classify(targetList);
            if(!CollectionUtils.isEmpty(couponClassify.getExpiredList())){
                redisService.addCouponsToCache(userId, couponClassify.getExpiredList(), CouponStatus.EXPIRED.getCode());

                //通过kafka修改db
                kafkaTemplate.send(Constant.TOPIC, JSON.toJSONString(new CouponKafkaMsg(CouponStatus.EXPIRED.getCode(),
                        couponClassify.getExpiredList().stream().map(Coupon::getId).collect(Collectors.toList()))));
                return couponClassify.getUsableList();
            }
        }
        return targetList;
    }

    @Override
    public Coupon acquireTemplate(AcquireTemplateRequest request) {
//        Coupon coupon = redisService.tryAcquireCouponFromCache(request.getTemplateId());
        return null;
    }

    @Override
    public SettlementInfo settlement(SettlementInfo info) {
        return null;
    }
}



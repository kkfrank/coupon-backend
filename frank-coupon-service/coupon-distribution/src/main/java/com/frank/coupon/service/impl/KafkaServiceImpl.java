package com.frank.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.frank.coupon.constant.Constant;
import com.frank.coupon.constant.CouponStatus;
import com.frank.coupon.dao.CouponDao;
import com.frank.coupon.entity.Coupon;
import com.frank.coupon.service.KafkaService;
import com.frank.coupon.vo.CouponKafkaMsg;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class KafkaServiceImpl implements KafkaService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaServiceImpl.class);

    @Autowired
    private CouponDao couponDao;

    /**
     * 数据回写到数据库里
     * @param record
     */
    @Override
    @KafkaListener(topics = { Constant.TOPIC }, groupId = "frank-coupon-1")
    public void consumeCouponKafkaMessage(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMsg = Optional.ofNullable(record.value());

        if(kafkaMsg.isPresent()){
            Object msg =  kafkaMsg.get();
            CouponKafkaMsg couponKafkaMsg = JSON.parseObject(msg.toString(), CouponKafkaMsg.class);
            logger.info("receive kafka mesg:{}", msg.toString());

            CouponStatus status = CouponStatus.of(couponKafkaMsg.getStatus());
            switch (status){
                case USABLE:
                    break;
                case USED:
                    processUsedCoupon(couponKafkaMsg, CouponStatus.USED);
                    break;
                case EXPIRED:
                    processExpiredCoupon(couponKafkaMsg, CouponStatus.EXPIRED);
                    break;
            }
        }
    }

    private void processUsedCoupon(CouponKafkaMsg couponKafkaMsg, CouponStatus status){
        //todo 发送短信给用户
        processCouponByStatus(couponKafkaMsg, status);
    }

    private void processExpiredCoupon(CouponKafkaMsg couponKafkaMsg, CouponStatus status){
        //todo 给用户推送信息
        processCouponByStatus(couponKafkaMsg, status);
    }


    private void processCouponByStatus(CouponKafkaMsg couponKafkaMsg, CouponStatus status){
        List<Coupon> couponList = couponDao.findAllById(couponKafkaMsg.getCouponIdList());

        if(CollectionUtils.isEmpty(couponList) || couponList.size()!= couponKafkaMsg.getCouponIdList().size()){
            logger.error("coupon info is not right:{}", couponKafkaMsg);
        }
        couponList.forEach(item->item.setStatus(status));
        couponDao.saveAll(couponList);
        logger.info("kafka msg save to db success: {}", couponKafkaMsg);
    }

}

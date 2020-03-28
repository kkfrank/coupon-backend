package com.frank.schedule;

import com.frank.dao.CouponTemplateDao;
import com.frank.entity.CouponTemplate;
import com.frank.service.impl.CouponTemplateServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 定时清理已过期模板
 *
 */
@Component
public class ScheduleTask {

    @Autowired
    private CouponTemplateDao couponTemplateDao;

    private Logger logger = LoggerFactory.getLogger(CouponTemplateServiceImpl.class);

    //每小时执行
    @Scheduled(fixedRate = 60*60*1000)
    public void offlineCouponTemplate(){
        logger.info("start to offlineCouponTemplate");
        List<CouponTemplate> templates = couponTemplateDao.findAllByExpired(false);
        if(CollectionUtils.isEmpty(templates)){
            logger.info("end offlineCouponTemplate, not have anyone");
            return;
        }
        List<CouponTemplate> expiredTemplates = new ArrayList<>(templates.size());

        Date nowDate = new Date();
        templates.forEach(item->{
            if(item.getRule().getExpiration().getDeadline() < nowDate.getTime()){
                expiredTemplates.add(item);
            }
        });

        if(CollectionUtils.isNotEmpty(expiredTemplates)){
            List<CouponTemplate> result = couponTemplateDao.saveAll(expiredTemplates);
            logger.info("expired Templates: {}", result);
        }
        logger.info("end offlineCouponTemplate, have");
    }
}

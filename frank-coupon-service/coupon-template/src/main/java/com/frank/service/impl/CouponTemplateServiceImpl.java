package com.frank.service.impl;

import com.frank.coupon.exception.CouponException;
import com.frank.dao.CouponTemplateDao;
import com.frank.entity.CouponTemplate;
import com.frank.service.AsyncService;
import com.frank.service.CouponTemplateService;
import com.frank.vo.CouponTemplateRequest;
import com.frank.vo.CouponTemplateSDK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CouponTemplateServiceImpl implements CouponTemplateService {

    private Logger logger = LoggerFactory.getLogger(CouponTemplateServiceImpl.class);

    @Autowired
    private CouponTemplateDao couponTemplateDao;
//    @Autowired
//    private CouponTemplateValidator couponTemplateValidator;

    @Autowired
    private AsyncService asyncService;

    @Override
    public CouponTemplate create(CouponTemplateRequest request) throws CouponException {
        if(!request.validate()){
            throw new CouponException("参数不合法");
        }
        if(couponTemplateDao.findByName(request.getName()) != null){
            throw new CouponException("已存在同名模板");
        }

        CouponTemplate template = build(request);
        template = couponTemplateDao.save(template);
        asyncService.asyncCreateCouponByTemplate(template);
        return template;
    }

    @Override
    public CouponTemplate findById(Integer id) throws CouponException {
         Optional<CouponTemplate> template = couponTemplateDao.findById(id);
         if(!template.isPresent()){
             throw new CouponException("template not exists");
         }
         return template.get();
    }

    @Override
    public List<CouponTemplateSDK> findAllUsable() {
        List<CouponTemplate> templates =  couponTemplateDao.findAllByAvailableAndExpired(true, false);
        return templates.stream().map(this::template2TemplateSDK).collect(Collectors.toList());
    }

    @Override
    public List<CouponTemplateSDK> findByIds(Collection<Integer> ids) {
        List<CouponTemplate> templates = couponTemplateDao.findAllById(ids);
        return templates.stream().map(this::template2TemplateSDK).collect(Collectors.toList());
    }

    @Override
    public List<CouponTemplateSDK> findAvailableByUserId(Long userId) {
        return null;
    }


    private CouponTemplateSDK template2TemplateSDK(CouponTemplate template){
        CouponTemplateSDK sdk = new CouponTemplateSDK();
        sdk.setId(template.getId());
        sdk.setName(template.getName());
        sdk.setLogo(template.getLogo());
        sdk.setDesc(template.getDesc());

        sdk.setCategory(template.getCategory().getCode());
        sdk.setProductLine(template.getProductLine().getCode());
        sdk.setKey(template.getKey());
        sdk.setTarget(template.getTarget().getCode());
        sdk.setRule(template.getRule());
        return sdk;
    }
    private CouponTemplate build(CouponTemplateRequest request){
        CouponTemplate couponTemplate = new CouponTemplate(
                request.getName(),
                request.getLogo(),
                request.getDesc(),
                request.getCategory(),
                request.getProductLine(),
                request.getCount(),
                request.getCreateBy(),
                request.getTarget(),
                request.getRule()
        );
        return couponTemplate;
    }
}

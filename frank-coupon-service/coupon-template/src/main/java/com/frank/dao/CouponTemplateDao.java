package com.frank.dao;

import com.frank.entity.CouponTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponTemplateDao extends JpaRepository<CouponTemplate, Integer>{
    /**
     *  根据模板名称查询模板
     *  @param name
     * @return
     */
    CouponTemplate findByName(String name);

    List<CouponTemplate> findAllByAvailableAndExpired(Boolean available, Boolean expired);

    List<CouponTemplate> findAllByExpired(Boolean expired);
}

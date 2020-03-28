package com.frank.converter;

import com.frank.coupon.constant.DistributeTarget;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;


@Convert
public class DistributeTargetConverter implements AttributeConverter<DistributeTarget, Integer> {
    @Override
    public Integer convertToDatabaseColumn(DistributeTarget distributeTarget) {
        return distributeTarget.getCode();
    }

    @Override
    public DistributeTarget convertToEntityAttribute(Integer dbData) {
        return DistributeTarget.of(dbData);
    }
}

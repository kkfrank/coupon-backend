package com.frank.converter;

import com.frank.coupon.constant.ProductLine;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

@Convert
public class ProductLineConverter implements AttributeConverter<ProductLine, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ProductLine productLine) {
        return productLine.getCode();
    }

    @Override
    public ProductLine convertToEntityAttribute(Integer dbData) {
        return ProductLine.of(dbData);
    }
}

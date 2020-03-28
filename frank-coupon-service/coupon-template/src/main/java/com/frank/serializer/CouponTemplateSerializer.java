package com.frank.serializer;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.frank.entity.CouponTemplate;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class CouponTemplateSerializer extends JsonSerializer<CouponTemplate> {

    @Override
    public void serialize(CouponTemplate template, JsonGenerator generator,
                          SerializerProvider serializerProvider) throws IOException {

        generator.writeStartObject();

        generator.writeStringField("id", template.getId().toString());
        generator.writeStringField("name", template.getName());

        generator.writeStringField("desc", template.getDesc());
        generator.writeStringField("logo", template.getLogo());

        generator.writeStringField("category", template.getCategory().getCode());
        generator.writeStringField("productLine", template.getProductLine().getCode().toString());

        generator.writeStringField("count", template.getCount().toString());
        generator.writeStringField("createTime", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(template.getCreateTime()));
        generator.writeStringField("createBy", template.getCreateBy().toString());

        generator.writeStringField("key", template.getKey() + String.format("%04d", template.getId()));

        generator.writeStringField("target", template.getTarget().getCode().toString());
        generator.writeStringField("rule", JSON.toJSONString(template.getRule()));

        generator.writeEndObject();
    }
}

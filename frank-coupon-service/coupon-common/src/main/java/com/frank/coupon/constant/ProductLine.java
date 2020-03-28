package com.frank.coupon.constant;

import java.util.stream.Stream;

public enum ProductLine {
    DAMAO(1, "大猫"),
    DABAO(2, "大宝");

    private Integer code;
    private String desc;

    ProductLine(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static ProductLine of(Integer code){
        return Stream.of(values())
                .filter(item -> item.getCode().equals(code))
                .findAny()
                .orElseThrow(()-> new IllegalArgumentException(code + " is not exists"));
    }

    public static void main(String[] args) {
        ProductLine d = ProductLine.DABAO;
        System.out.println(d.toString());
        System.out.println(String.format("%04d", 1234567890));
    }
}

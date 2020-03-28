package com.frank.coupon.constant;

import java.util.stream.Stream;

public enum CouponStatus {
    USABLE(1, "可用的"),
    USED(2, "已使用的"),
    EXPIRED(3, "过期的(包含未被使用过期的)");

    private Integer code;
    private String desc;

    CouponStatus(Integer code, String desc) {
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

    public static CouponStatus of(Integer code){
       return  Stream.of(values())
                .filter(item-> item.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists"));
    }
}

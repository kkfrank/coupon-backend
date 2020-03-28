package com.frank.coupon.constant;

import java.util.stream.Stream;

public enum DistributeTarget {
    SINGLE(1, "单用户"),//用户需要自己领取
    MULTI(2, "多用户");//会自动分发给用户

    private Integer code;
    private String desc;

    DistributeTarget(Integer code, String desc) {
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

    public static DistributeTarget of(Integer code){
        return Stream.of(values())
                .filter(item -> item.code.equals(code))
                .findAny()
                .orElseThrow(()-> new IllegalArgumentException(code + " is not exist"));
    }
}

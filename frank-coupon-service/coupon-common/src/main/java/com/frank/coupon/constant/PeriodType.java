package com.frank.coupon.constant;

import java.util.stream.Stream;

public enum PeriodType {
    REGULAR(1, "固定的(固定的日期)"),
    SHIFT(2, "变动的(以领取之日开始计算)");
    private Integer code;
    private String desc;

    PeriodType(Integer code, String desc) {
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

    public static PeriodType of(Integer code){
        return Stream.of(values())
                .filter(item -> item.getCode().equals(code))
                .findAny()
                .orElseThrow(()-> new IllegalArgumentException(code + " is not exists"));
    }
}

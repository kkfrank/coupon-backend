package com.frank.coupon.constant;

import java.util.stream.Stream;

/**
 * 商品类型
 */
public enum GoodsType {
    WENYU(1, "文娱"),
    SHENGXIAN(2, "生鲜"),
    JIAJU(3, "家居"),
    OTHRES(99, "其他"),
    ALL(100, "全品类");
    private Integer code;
    private String desc;

    GoodsType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public static GoodsType of(Integer code){
        return Stream.of(values())
                .filter(item -> item.code.equals(code))
                .findAny()
                .orElseThrow(()-> new IllegalArgumentException(code + " is not exist"));
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
}

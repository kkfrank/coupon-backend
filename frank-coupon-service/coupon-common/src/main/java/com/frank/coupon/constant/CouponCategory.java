package com.frank.coupon.constant;


import java.util.stream.Stream;

public enum CouponCategory {
    MANJIAN("001", "满减券"),
    ZHEKOU("002", "折扣券"),
    LIJIAN("003", "李健券");

    private String code;
    private String desc;

    CouponCategory(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public static CouponCategory of(String code){
//        if(!StringUtils.isEmpty(code)){
//            throw new NullPointerException();
//        }
       // Objects.requireNonNull(code);

//        CouponCategory[]  list = CouponCategory.values();
//        for(CouponCategory item : list){
//            if(item.getCode().equals(code)){
//                return item;
//            }
//        }
//        throw new IllegalArgumentException(code + " not exists");

        return Stream.of(values())
                .filter(item -> item.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists"));
    }
}

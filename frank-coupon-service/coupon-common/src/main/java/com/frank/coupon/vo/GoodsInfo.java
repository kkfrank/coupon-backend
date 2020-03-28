package com.frank.coupon.vo;

/**
 * fake 商品信息
 */
public class GoodsInfo {

    //GoodsType
    private Integer type;

    private Double price;

    private Integer count;

    //todo name and others


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

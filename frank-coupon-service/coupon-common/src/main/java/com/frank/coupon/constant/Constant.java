package com.frank.coupon.constant;

/**
 * 通用常量
 */
public class Constant {

    //kafka 消息的topic
    public static final String TOPIC = "frank_user_coupon_op";

    // Redis key 前缀
    public static class RedisPrefix{

        //优惠券码key前缀
        public static final String COUPON_TEMPLATE = "frank_coupon_template_code_";

        public static final String USER_COUPON_USABLE = "frank_coupon_usable_";

        //用户当前已使用的优惠券key前缀
        public static final String USER_COUPON_USED = "frank_user_coupon_used_";

        public static final String USER_COUPON_EXPIRED = "frank_user_coupon_expired_";


    }
}

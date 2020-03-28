package com.frank.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.frank.coupon.constant.Constant;
import com.frank.coupon.constant.CouponStatus;
import com.frank.coupon.entity.Coupon;
import com.frank.coupon.exception.CouponException;
import com.frank.coupon.service.RedisService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RedisServiceImpl implements RedisService {
    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public List<Coupon> getCachedCoupons(Long userId, Integer status) {
        String redisKey = status2RedisKey(status, userId);
        List<String> list = redisTemplate.opsForHash().values(redisKey).stream()
                                        .map(item -> Objects.toString(item, null))
                                        .collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(list)){
            saveEmptyCouponToCache(userId, Collections.singletonList(status));
            return Collections.emptyList();
        }
        //redisTemplate.opsForList().rightPushAll(reidsKey, couponList);
        return list.stream().map(item -> JSON.parseObject(item, Coupon.class))
                .collect(Collectors.toList());
    }


    /**
     * 优惠券缓存信息结构：KV
     * K: status -> redisKey
     * V: { couponId: 序列号的Coupon }
     * @param userId
     * @param statusList
     */
    @Override
    public void saveEmptyCouponToCache(Long userId, List<Integer> statusList) {
        Map<String, String> invalidCouponMap = new HashMap<>();
        Coupon coupon = Coupon.invalidCoupon();
        invalidCouponMap.put(coupon.getId().toString(), JSON.toJSONString(invalidCouponMap));

        //使用SessionCallback吧数据命令放到redis的pipeline
        SessionCallback sessionCallback = new SessionCallback() {
            @Nullable
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                for (Integer status: statusList){
                    String redisKey = status2RedisKey(status, userId);
                    operations.opsForHash().putAll(redisKey, invalidCouponMap);
                }
                return null;
            }
        };
        List list= redisTemplate.executePipelined(sessionCallback);
        logger.info("Pipeline execute result :{}", JSON.toJSONString(list));
    }


    @Override
    public String tryAcquireCouponFromCache(Integer templateId) {
        String redisKey = String.format("%s%s", Constant.RedisPrefix.COUPON_TEMPLATE, templateId);
        //左右都可以
        String couponCode = redisTemplate.opsForList().rightPop(redisKey);
        logger.info("acquire coupon code:{},{},{}", templateId, redisKey, couponCode);
        return couponCode;
    }

    @Override
    public Integer addCouponsToCache(Long userId, List<Coupon> couponList, Integer status) throws CouponException {
        Integer result = -1;
        CouponStatus couponStatus = CouponStatus.of(status);
        switch (couponStatus){
            case USABLE:
                result = addCouponToCacheForUsable(userId, couponList);
                break;
            case USED:
                result = addCouponToCacheForUsed(userId, couponList);
                break;
            case EXPIRED:
                result = addCouponToCacheForExpired(userId, couponList);
                break;
        }
        return result;
    }

    /**
     * 新增的加优惠券 添加到cache
     * @return
     */
    private Integer addCouponToCacheForUsable(Long userId, List<Coupon> couponList){
        HashMap map = new HashMap();
        for (Coupon coupon: couponList){
            map.put(coupon.getId(), JSON.toJSONString(coupon));
        }
        String redisKey = status2RedisKey(CouponStatus.USABLE.getCode(), userId);
        redisTemplate.opsForHash().putAll(redisKey, map);
        logger.info("add {} Coupon To Cache For Usable: {}, {}", couponList.size(), userId, redisKey);

        redisTemplate.expire(redisKey, getRandomExpiredTime(1, 2), TimeUnit.SECONDS);//1-2小时
        return couponList.size();
    }

    private Integer addCouponToCacheForUsed(Long userId, List<Coupon> toUsedCouponList) throws CouponException {
        // 1. check toAddCouponList (allUsableList的个数一定是大于1的)
        if(CollectionUtils.isEmpty(toUsedCouponList)){
            throw new CouponException("params coupon is not empty");
        }
        List<Coupon> usableList = getCachedCoupons(userId, CouponStatus.USABLE.getCode());
        List<Integer> usableIdList = usableList.stream().map(Coupon::getId).collect(Collectors.toList());
        List<Integer> toUsedIdList = toUsedCouponList.stream().map(Coupon::getId).collect(Collectors.toList());

        assert usableList.size() > toUsedIdList.size();

        for (Integer id : usableIdList){
            if(!usableIdList.contains(id)){
                logger.error("userId:{}, avaliable couponId list:{}, to used couponId list:{}", userId, usableIdList, usableIdList);
                throw new CouponException("params coupon is not valid");
            }
        }
        SessionCallback sessionCallback = new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String usableRedisKey = status2RedisKey(CouponStatus.USABLE.getCode(), userId);
                String usedRedisKey = status2RedisKey(CouponStatus.USED.getCode(), userId);

                HashMap<String, String> toAddMap = new HashMap();
                toUsedCouponList.forEach(item->{
                    toAddMap.put(item.getId().toString(), JSON.toJSONString(item));
                });

                //2. add toAddCouponList to usedCoupon redis
                operations.opsForHash().putAll(usedRedisKey, toAddMap);

                //3. delete toAddCouponList from usableCoupon redis
                operations.opsForHash().delete(usableRedisKey, toUsedIdList.toArray());

                // 4.reset expire time to usedCoupon and usableCoupon
                operations.expire(usedRedisKey, getRandomExpiredTime(1,2), TimeUnit.SECONDS);
                operations.expire(usableRedisKey, getRandomExpiredTime(1,2), TimeUnit.SECONDS);
                return null;
            }
        };
        redisTemplate.executePipelined(sessionCallback);
        logger.info("addCouponToCacheForUsed:{}", toUsedIdList);
        return toUsedCouponList.size();
    }

    /**
     * 会影响expired cache 和 usable cache。 已使用过的优惠券不存在过期概念
     */
    private Integer addCouponToCacheForExpired(Long userId, List<Coupon> toExpiredList) throws CouponException {
        if(CollectionUtils.isEmpty(toExpiredList)){
            throw new CouponException("params coupon is not empty");
        }

        List<Coupon> usableCouponList = getCachedCoupons(userId, CouponStatus.USABLE.getCode());
        List<Coupon> expiredCouponList = getCachedCoupons(userId, CouponStatus.EXPIRED.getCode());

        assert usableCouponList.size() > toExpiredList.size();

        List<Integer> usableIds = usableCouponList.stream().map(Coupon::getId).collect(Collectors.toList());

        List<Integer> toExpiredIds = toExpiredList.stream().map(Coupon::getId).collect(Collectors.toList());

        //1. check expired coupon list in usable or used and not in expired list
        for (Integer id: toExpiredIds){
            if(!usableIds.contains(id)){
                throw new CouponException("prams coupon:"+ id+  "is not valid");
            }
        }

        SessionCallback sessionCallback = new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String usableRedisKey = status2RedisKey(CouponStatus.USABLE.getCode(), userId);
                String expiredRedisKey = status2RedisKey(CouponStatus.EXPIRED.getCode(), userId);

                HashMap<String, String> map = new HashMap<>();
                expiredCouponList.forEach(item->{
                    map.put(item.getId().toString(), JSON.toJSONString(item));
                });
                //2. add expired coupon list to redis
                operations.opsForHash().putAll(expiredRedisKey, map);

                //3. remove expired coupon list from usable redis
                operations.opsForHash().delete(usableRedisKey, toExpiredIds.toArray());

                //4. reset three type redis
                operations.expire(usableRedisKey, getRandomExpiredTime(1, 2), TimeUnit.SECONDS);
                operations.expire(expiredRedisKey, getRandomExpiredTime(1, 2), TimeUnit.SECONDS);
                return null;
            }
        };
        redisTemplate.executePipelined(sessionCallback);
        logger.info("addCouponToCacheForExpired:{}", toExpiredIds);
        return toExpiredList.size();
    }

    private String status2RedisKey(Integer status, Long userId){
        CouponStatus couponStatus = CouponStatus.of(status);

        switch (couponStatus){
            case USED:
                return String.format("%s%s", Constant.RedisPrefix.USER_COUPON_USED, userId);
            case USABLE:
                return String.format("%s%s", Constant.RedisPrefix.USER_COUPON_USABLE, userId);
            case EXPIRED:
                return String.format("%s%s", Constant.RedisPrefix.USER_COUPON_EXPIRED, userId);
        }
        return null;
    }


    /**
     * 返回[min, max]
     * 避免缓存雪崩: key在同一时间失效
     * @param min
     * @param max
     * @return
     */
    private Long getRandomExpiredTime(Integer min, Integer max){
        return RandomUtils.nextLong(min * 60 * 60, max * 60 * 60);
    }
    public static void main(String[] args) {
        HashMap hash = new HashMap();


        Map<String, String> invalidCouponMap = new HashMap<>();
        Coupon coupon = Coupon.invalidCoupon();
        invalidCouponMap.put(coupon.getId().toString(), JSON.toJSONString(invalidCouponMap));

        System.out.println(JSON.toJSONString(invalidCouponMap));
        System.out.println(invalidCouponMap.values());

        hash.put("age","212");
        System.out.println(hash.values());

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        System.out.println(list);
        System.out.println(list.toArray());
        System.out.println(Arrays.asList(1,2,3));

        List l = Collections.emptyList();
        System.out.println(CollectionUtils.isEmpty(l));
    }
}

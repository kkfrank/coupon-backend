package com.frank.coupon.filter;


import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

//限流过滤器
@Component
public class RateLimitFilter extends AbstractPreZuulFilter {
    private Logger logger = LoggerFactory.getLogger(TokenFilter.class);

    //每秒可以获取到两个令牌
    private RateLimiter rateLimiter = RateLimiter.create(2);

    @Override
    protected Object cRun() {
        HttpServletRequest request = context.getRequest();
        if(rateLimiter.tryAcquire()){
            logger.info("get rate token success");
            return success();
        }
        logger.error("rate limit:{}", request.getRequestURI());
        return fail(402,"rate limit");
    }

    @Override
    public int filterOrder() {
        return 1;
    }
}

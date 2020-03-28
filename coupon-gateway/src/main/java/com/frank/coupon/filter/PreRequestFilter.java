package com.frank.coupon.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PreRequestFilter extends AbstractPreZuulFilter {
    private Logger logger = LoggerFactory.getLogger(TokenFilter.class);

    @Override
    protected Object cRun() {
        context.set("startTime", System.currentTimeMillis());
        return success();
    }

    @Override
    public int filterOrder() {
        return 0;
    }
}

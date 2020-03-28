package com.frank.coupon.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AccessLogFilter extends AbstractPostZuulFilter {
    private Logger logger = LoggerFactory.getLogger(TokenFilter.class);

    @Override
    protected Object cRun() {
        HttpServletRequest request = context.getRequest();
        Long startTime = (Long)context.get("startTime");

        String uri = request.getRequestURI();
        Long duration = System.currentTimeMillis() - startTime;

        logger.info("uri:{}, duration:{}", uri, duration);
        return success();
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
    }
}

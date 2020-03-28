package com.frank.coupon.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class TokenFilter extends AbstractPreZuulFilter{
    private Logger logger = LoggerFactory.getLogger(TokenFilter.class);

    @Override
    protected Object cRun() {
        HttpServletRequest request = context.getRequest();
        logger.info("%s request to %s", request.getMethod(), request.getRequestURL().toString());
        Object token = request.getParameter("token");

        if(token == null){
            logger.error("token is empty");
            return fail(401, "token is empty");
        }
        return success();
    }

    @Override
    public int filterOrder() {
        return 2;
    }
}

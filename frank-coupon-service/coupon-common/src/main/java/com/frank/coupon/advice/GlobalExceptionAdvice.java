package com.frank.coupon.advice;

import com.frank.coupon.exception.CouponException;
import com.frank.coupon.vo.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionAdvice {

    //对CouponException 进行统一处理
    @ExceptionHandler(value = CouponException.class)
    public CommonResponse<String> handleCouponException(HttpServletRequest request, CouponException ex){
        CommonResponse response = new CommonResponse<>(-1, "business error");
        response.setData(ex.getMessage());
        return response;
    }
}

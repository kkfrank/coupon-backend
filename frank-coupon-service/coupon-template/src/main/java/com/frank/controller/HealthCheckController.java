package com.frank.controller;


import com.frank.coupon.exception.CouponException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/health")
public class HealthCheckController {

    @Autowired
    private DiscoveryClient discoveryClient;

    //获取服务id的信息
    @Autowired
    private Registration registration;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public String healthCheck(){
        return "CouponTemplate is ok";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/exception")
    public String exception() throws CouponException {
        throw new CouponException("test CouponException");
    }

    /**
     * 获取Eureka server上微服务的元信息，大约等待2分钟，才能获取到注册信息
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/info")
    public List<Map<String, Object>> info(){
        List<ServiceInstance> instances = discoveryClient.getInstances(registration.getServiceId());

        List<Map<String, Object>> result = new ArrayList<>(instances.size());
        instances.forEach(item->{
            Map<String, Object> info = new HashMap<>();
            info.put("servicedId", item.getServiceId());
            info.put("instanceId", item.getInstanceId());
            info.put("port", item.getPort());
            result.add(info);
        });
        return result;
    }
}

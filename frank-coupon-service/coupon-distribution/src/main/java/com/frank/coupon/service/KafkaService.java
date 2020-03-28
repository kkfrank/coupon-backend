package com.frank.coupon.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface KafkaService {
    void consumeCouponKafkaMessage(ConsumerRecord<?,?> record);
}

package com.frank.config;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

//异步任务线程池
@EnableAsync
@Configuration
public class AsyncPoolConfig implements AsyncConfigurer {
    private Logger logger = LoggerFactory.getLogger(AsyncPoolConfig.class);

    @Bean
    @Nullable
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);// 设置核心线程池线程的数量，默认是1
        executor.setMaxPoolSize(20); // 线程池最大的线程数量，当核心线程都被用光，且缓冲队列是满的之后才申请
        executor.setQueueCapacity(20); // 缓冲队列中任务的个数
        executor.setKeepAliveSeconds(60); // 超出核心线程数的其他线程在空闲时的最大的存活时间
        executor.setThreadNamePrefix("frank_"); //线程池中的线程名称前缀

        executor.setWaitForTasksToCompleteOnShutdown(true);//程序关闭后，是否等待异步任务完成, 等待的时间需要配置， 默认false，
        executor.setAwaitTerminationSeconds(60);// 等待的时间，默认0

        //拒绝策略 线程池中的最大线程线程数用光，且缓冲队列也满了，可以选择的策略
        // DiscardPolicy: 丢弃需要添加的新的任务
        // AbortPolicy: 直接抛出异常
        // DiscardOldestPolicy： 丢弃阻塞队列的队头任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }

    @Nullable
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }

    class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler{
        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
            logger.error("AsyncException:{}, Method:{}, Params:{}", throwable, method, JSON.toJSONString(objects));

            throwable.printStackTrace();
            //TODO 发送邮件或者短信
        }
    }
}

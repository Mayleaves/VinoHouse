package com.VinoHouse.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自定义定时任务类
 */
@Component
@Slf4j
public class MyTask {

    /**
     * 定时任务 每隔 5 秒触发一次
     */
    //@Scheduled(cron = "0/5 * * * * ?")  // 这里仅作测试，因此注释掉
    public void executeTask(){
        log.info("定时任务开始执行：{}", new Date());
    }

}

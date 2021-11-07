package com.jornah.job;

import com.jornah.service.es.EsContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author licong
 * @date 2021/11/8 00:20
 */
@Component
@Slf4j
public class SynData2Es {
    @Autowired
    private EsContentService esContentService;

//    @Scheduled(cron = "00 00 00 ? * *")
    @Scheduled(cron = "0 0 0 0/1 * *")
    public void syn(){
        log.info("runing");
//        esContentService.exportDataToEs();
    }
}

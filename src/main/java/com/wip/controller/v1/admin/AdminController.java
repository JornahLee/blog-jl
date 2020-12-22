package com.wip.controller.v1.admin;

import com.wip.service.es.EsContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private EsContentService esContentService;

    @RequestMapping("/es/import")
    @ResponseStatus(HttpStatus.CREATED)
    public void exportDataToEs(){
        esContentService.exportDataToEs();
    }

}

package com.jornah.controller.v1.admin;

import com.jornah.service.es.EsContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

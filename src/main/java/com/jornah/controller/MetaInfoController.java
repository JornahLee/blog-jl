package com.jornah.controller;

import com.jornah.model.newP.Category;
import com.jornah.model.newP.Tag;
import com.jornah.service.MetaInfoService;
import com.jornah.utils.APIResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("文章元数据")
@RestController
@RequestMapping("/blog/meta")
@CrossOrigin
public class MetaInfoController extends BaseController {
    @Autowired
    private MetaInfoService metaInfoService;


    @ApiOperation("所有标签")
    @GetMapping(value = "/category/list")
    public APIResponse<List<Category>> listCategory(){
        return APIResponse.success(metaInfoService.getAllCategory());

    }

    @ApiOperation("所有分类")
    @GetMapping(value = "/tag/list")
    public APIResponse<List<Tag>> listTag(){
        return APIResponse.success(metaInfoService.getAllTag());
    }

}

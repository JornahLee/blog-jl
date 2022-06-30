package com.jornah.controller;

import com.jornah.model.entity.Category;
import com.jornah.model.entity.Tag;
import com.jornah.model.qo.MetaInfoQo;
import com.jornah.service.MetaInfoService;
import com.jornah.utils.APIResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public APIResponse<List<Category>> listCategory() {
        return APIResponse.success(metaInfoService.getAllCategory());

    }

    @ApiOperation("所有分类")
    @GetMapping(value = "/tag/list")
    public APIResponse<List<Tag>> listTag() {
        return APIResponse.success(metaInfoService.getAllTag());
    }

    @ApiOperation("新增标签")
    @PutMapping(value = "/tag")
    public APIResponse<Tag> newTag(@RequestParam String name) {
        return APIResponse.success(metaInfoService.addTag(name));
    }
    @ApiOperation("删除标签")
    @DeleteMapping(value = "/tag/{id}")
    public APIResponse<?> deleteTag(@PathVariable Long id) {
        return APIResponse.success(metaInfoService.deleteTag(id));
    }

    @ApiOperation("新增分类")
    @PutMapping(value = "/category")
    public APIResponse<Category> newCate(@RequestParam String name) {
        return APIResponse.success(metaInfoService.addCategory(name));
    }
    @ApiOperation("删除分类")
    @DeleteMapping(value = "/cate/{id}")
    public APIResponse<?> deleteCate(@PathVariable Long id) {
        return APIResponse.success(metaInfoService.deleteCate(id));
    }

    @ApiOperation("保存文章 元数据信息")
    @PostMapping(value = "/save")
    public APIResponse<?> saveMetaInfo(@RequestBody MetaInfoQo qo) {
        metaInfoService.saveMeta(qo);
        return APIResponse.success();
    }


}

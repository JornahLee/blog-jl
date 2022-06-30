package com.jornah.service;

import com.jornah.model.entity.Category;
import com.jornah.model.entity.Tag;
import com.jornah.model.qo.MetaInfoQo;

import java.util.List;

public interface MetaInfoService {
    List<Tag> getAllTag();

    List<Category> getAllCategory();

    Category addCategory(String cateName);

    int deleteCate(Long id);

    Tag addTag(String name);

    int deleteTag(Long id);

    void saveMeta(MetaInfoQo qo);
}

package com.jornah.service;

import com.jornah.model.entity.Category;
import com.jornah.model.entity.Tag;
import com.jornah.model.qo.MetaInfoQo;

import java.util.List;

public interface MetaInfoService {
    List<Tag> getAllTag();

    List<Category> getAllCategory();

    Category addCategory(String cateName);

    Tag addTag(String name);

    void saveMeta(MetaInfoQo qo);
}

package com.jornah.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jornah.dao.CategoryDao;
import com.jornah.dao.TagDao;
import com.jornah.model.entity.Category;
import com.jornah.model.entity.Tag;
import com.jornah.model.qo.MetaInfoQo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetaInfoServiceImpl implements MetaInfoService {

    @Autowired
    private TagDao tagDao;
    @Autowired
    private CategoryDao categoryDao;

    @Override
    public List<Tag> getAllTag() {
        return tagDao.selectList(new QueryWrapper<>());
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryDao.selectList(new QueryWrapper<>());
    }

    @Override
    public Category addCategory(String cateName) {
        Category category = new Category();
        category.setName(cateName);
        categoryDao.insert(category);
        return category;
    }

    @Override
    public Tag addTag(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        tagDao.insert(tag);
        return tag;
    }

    @Override
    public void saveMeta(MetaInfoQo qo){
        categoryDao.deleteAllMapBy(qo.getArticleId());
        categoryDao.insertMap(qo.getArticleId(),qo.getCateId());
        // TODO  tag还未实现
    }
}

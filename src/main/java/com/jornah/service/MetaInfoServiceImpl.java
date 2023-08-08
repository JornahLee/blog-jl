package com.jornah.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jornah.dao.CategoryDao;
import com.jornah.dao.TagDao;
import com.jornah.model.entity.Category;
import com.jornah.model.entity.Tag;
import com.jornah.model.qo.MetaInfoQo;
import com.jornah.service.cache.impl.CacheHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetaInfoServiceImpl implements MetaInfoService {

    @Autowired
    private TagDao tagDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private CacheHolder cacheHolder;


    @Override
    public List<Tag> getAllTag() {
//        List<Tag> tags = tagDao.selectList(new QueryWrapper<>());
//        return tags;
        return cacheHolder.getTagCache()
                .getOrSaveCache(() -> tagDao.selectList(new QueryWrapper<>()));
    }

    @Override
    public List<Category> getAllCategory() {
        return cacheHolder.getCategoryCache()
                .getOrSaveCache(() -> categoryDao.selectList(new QueryWrapper<>()));
    }

    @Override
    public Category addCategory(String cateName) {
        Category category = new Category();
        category.setName(cateName);
        categoryDao.insert(category);
        return category;
    }

    @Override
    public int deleteCate(Long id) {
        return categoryDao.deleteById(id);
    }

    @Override
    public Tag addTag(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        tagDao.insert(tag);
        return tag;
    }

    @Override
    public int deleteTag(Long id) {
        return tagDao.deleteById(id);
    }

    @Override
    public void saveMeta(MetaInfoQo qo) {
        categoryDao.deleteAllMapBy(qo.getArticleId());
        categoryDao.insertMap(qo.getArticleId(), qo.getCateId());

        tagDao.deleteAllMapBy(qo.getArticleId());
        qo.getTagIds().forEach(tagId -> {
            tagDao.insertMap(qo.getArticleId(), tagId);
        });

    }
}

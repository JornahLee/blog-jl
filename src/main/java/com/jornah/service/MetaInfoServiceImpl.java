package com.jornah.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jornah.dao.CategoryDao;
import com.jornah.dao.TagDao;
import com.jornah.model.newP.Category;
import com.jornah.model.newP.Tag;
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
}

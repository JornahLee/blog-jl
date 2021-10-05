package com.jornah.service;

import com.jornah.model.Music;
import com.jornah.model.newP.Category;
import com.jornah.model.newP.Tag;
import com.jornah.model.qo.MusicUploadQo;

import java.io.IOException;
import java.util.List;

public interface MetaInfoService {
    List<Tag> getAllTag();

    List<Category> getAllCategory();
}

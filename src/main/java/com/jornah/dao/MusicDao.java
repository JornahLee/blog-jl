package com.jornah.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jornah.model.entity.Music;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author licong
 * @date 2021/10/2 01:55
 */
@Repository
@Mapper
public interface MusicDao extends BaseMapper<Music> {
}

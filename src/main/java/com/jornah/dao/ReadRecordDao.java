package com.jornah.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jornah.model.qo.ReadRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author licong
 * @date 2021/10/30 23:41
 */
@Mapper
@Repository
public interface ReadRecordDao extends BaseMapper<ReadRecord> {
    @Select("select * from read_record order by start_read_time desc limit 10")
    List<ReadRecord> getRecentRead();

    @Select("insert into read_record(article_id,title,read_duration,start_read_time) " +
            "values(#{articleId},#{title},#{readDuration},#{startReadTime})" +
            "ON DUPLICATE KEY UPDATE read_duration=#{readDuration}," +
            "title=#{title},start_read_time=#{startReadTime}")
    ReadRecord insertOrUpdate(ReadRecord readRecord);
}

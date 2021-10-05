package com.jornah.model.converter;

import com.jornah.model.dto.ArticleSaveBo;
import com.jornah.model.newP.Article;
import com.jornah.model.newP.User;
import com.jornah.model.vo.ArticleVo;
import com.jornah.model.vo.UserVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author licong
 * @date 2021/10/3 10:08
 */
@Mapper
public interface ArticleConverter {
    ArticleConverter INSTANCE = Mappers.getMapper( ArticleConverter.class );

    Article toEntity(ArticleSaveBo saveBo);
    ArticleVo toVo(Article article);


}

/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/25 16:48
 **/
package com.jornah.service.article;

import com.github.pagehelper.PageInfo;
import com.jornah.model.dto.ArticleSaveBo;
import com.jornah.model.qo.ArticleQo;
import com.jornah.model.vo.ArticleMetaInfo;
import com.jornah.model.vo.ArticleVo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 文章相关Service接口
 */
public interface ArticleService  {

    long saveOrUpdate(ArticleSaveBo articleSaveBo);

    ArticleVo getArticleBy(Long arId);

    PageInfo<ArticleVo> getArticlesOrderBy(ArticleQo articleQo);

    @Transactional
    void deleteBy(Long arId);

    PageInfo<ArticleVo> getArticleByCate(Long cateId, int pageNum, int pageSize);

    PageInfo<ArticleVo> getArticleByTag(Long tagId, int pageNum, int pageSize);

    List<ArticleVo> getRecommendArticle(int size);

    void updateArticleHits(Integer cid, Integer chits);

    boolean isFromSameIp(Integer cid, HttpServletRequest request);

    @Async
    void logVisit(Integer cid, String detail, HttpServletRequest request);

    ArticleMetaInfo getArticleMetaInfo(Long articleId);
}

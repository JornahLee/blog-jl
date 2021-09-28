/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/25 16:48
 **/
package com.jornah.service.article;

import com.github.pagehelper.PageInfo;
import com.jornah.model.dto.ArticleSaveBo;
import com.jornah.model.newP.Article;
import com.jornah.model.qo.ArticleQo;
import com.jornah.model.vo.ArticleVo;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;

/**
 * 文章相关Service接口
 */
public interface ArticleService  {

    long addArticle(ArticleSaveBo articleSaveBo);

    ArticleVo getArticleBy(Long arId);

    PageInfo<Article> getArticlesOrderBy(ArticleQo articleQo);

    void updateArticleHits(Integer cid, Integer chits);

    boolean isFromSameIp(Integer cid, HttpServletRequest request);

    @Async
    void logVisit(Integer cid, String detail, HttpServletRequest request);
}

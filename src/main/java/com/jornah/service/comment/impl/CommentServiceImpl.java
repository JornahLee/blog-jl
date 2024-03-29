/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/31 15:40
 **/
package com.jornah.service.comment.impl;

import com.jornah.constant.ErrorConstant;
import com.jornah.dao.CommentDao;
import com.jornah.exception.BusinessException;
import com.jornah.model.entity.Comment;
import com.jornah.service.article.ArticleService;
import com.jornah.service.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private ArticleService articleService;

    private static final Map<String,String> STATUS_MAP = new ConcurrentHashMap<>();

    /**
     * 评论状态：正常
     */
    private static final String STATUS_NORMAL = "approved";
    /**
     * 评论状态：不不显示
     */
    private static final String STATUS_BLANK = "not_audit";

    static {
        STATUS_MAP.put("approved",STATUS_NORMAL);
        STATUS_MAP.put("not_audit",STATUS_BLANK);
    }


    @Override
    @Transactional
    public void addComment(Comment comments) {
//        String msg = null;
//
//        if (null == comments) {
//            msg = "评论对象为空";
//        }
//
//        if (StringUtils.isBlank(comments.getAuthor())) {
//            comments.setAuthor("热心网友");
//        }
//        if (StringUtils.isNotBlank(comments.getEmail()) && !TaleUtils.isEmail(comments.getEmail())) {
//            msg =  "请输入正确的邮箱格式";
//        }
//        if (StringUtils.isBlank(comments.getContent())) {
//            msg = "评论内容不能为空";
//        }
//        if (comments.getContent().length() < 1 || comments.getContent().length() > 2000) {
//            msg = "评论字数在1-2000个字符";
//        }
//        if (null == comments.getCid()) {
//            msg = "评论文章不能为空";
//        }
//        if (msg != null) {
//            throw BusinessException.withErrorCode(msg);
//        }
//
//        Article article = articleService.getArticleById(comments.getCid());
//        if (null == article) {
//            throw BusinessException.withErrorCode("该文章不存在");
//        }
//
//        comments.setOwnerId(article.getAuthorId());
//        comments.setStatus(STATUS_MAP.get(STATUS_BLANK));
//        comments.setCreated(Instant.now());
//        commentDao.addComment(comments);
//
//        Article temp = new Article();
//        temp.setCid(article.getCid());
//        Integer count = article.getCommentsNum();
//        if (null == count) {
//            count = 0;
//        }
//        temp.setCommentsNum(count + 1);
//        articleService.updateContentByCid(temp);

    }

    @Override
    public List<Comment> getCommentsByCId(Integer cid) {
        if (null == cid) {
            throw BusinessException.of(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        return commentDao.getCommentByCId(cid);
    }

    @Override
    public Comment getCommentById(Integer coid) {
        if (null == coid) {
            throw BusinessException.of(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        return commentDao.getCommentById(coid);
    }

    @Override
    public void updateCommentStatus(Integer coid, String status) {
        if (null == coid) {
            throw BusinessException.of(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        commentDao.updateCommentStatus(coid, status);
    }

    @Override
    public void deleteComment(Integer coid) {
        if (null == coid) {
            throw BusinessException.of(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        commentDao.deleteComment(coid);
    }
}

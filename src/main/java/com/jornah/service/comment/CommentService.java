/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/31 15:40
 **/
package com.jornah.service.comment;

import com.jornah.model.entity.Comment;

import java.util.List;

/**
 * 评论相关Service接口
 */
public interface CommentService {

    /**
     * 添加评论
     * @param comments
     */
    void addComment(Comment comments);

    /**
     * 通过文章ID获取评论
     * @param cid
     * @return
     */
    List<Comment> getCommentsByCId(Integer cid);


    /**
     * 通过ID获取评论
     * @param coid
     * @return
     */
    Comment getCommentById(Integer coid);

    /**
     * 更新评论状态
     * @param coid
     * @param status
     */
    void updateCommentStatus(Integer coid, String status);

    /**
     * 删除评论
     * @param id
     */
    void deleteComment(Integer id);

}

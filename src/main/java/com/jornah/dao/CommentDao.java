/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/31 15:42
 **/
package com.jornah.dao;

import com.jornah.model.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 评论相关Dao接口
 */
@Mapper
@Repository
public interface CommentDao {

    /**
     * 添加评论
     * @param comments
     */
    void addComment(Comment comments);

    /**
     * 根据文章ID获取评论
     * @param cid
     * @return
     */
    List<Comment> getCommentByCId(@Param("cid") Integer cid);


    /**
     * 删除评论
     * @param coid
     */
    void deleteComment(@Param("coid") Integer coid);

    /**
     * 获取评论总数
     * @return
     */
    Long getCommentCount();


    /**
     * 通过ID获取评论
     * @param coid
     * @return
     */
    Comment getCommentById(@Param("coid") Integer coid);

    /**
     * 更新评论状态
     * @param coid
     * @param status
     */
    void updateCommentStatus(@Param("coid") Integer coid, @Param("status") String status);
}

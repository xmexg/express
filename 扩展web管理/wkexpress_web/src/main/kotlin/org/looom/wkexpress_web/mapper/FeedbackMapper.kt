package org.looom.wkexpress_web.mapper

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Select
import org.looom.wkexpress_web.model.FeedbackModel

/**
 * 反馈相关
 *
 *   插入一条主题帖
 *   ```sql
 *   INSERT INTO feedback (openid, is_admin, parent_id, content, created_at)
 *   VALUES ('user12345', FALSE, NULL, 'This is my feedback about...', NOW());
 *   ```
 *   插入一条评论（普通用户）
 *   ```sql
 *   INSERT INTO feedback (openid, is_admin, parent_id, content, created_at)
 *   VALUES ('user12345', FALSE, 1, 'Here is my additional comment.', NOW());
 *   ```
 *   插入管理员回复
 *   ```sql
 *   INSERT INTO feedback (openid, is_admin, parent_id, content, created_at)
 *   VALUES ('admin56789', TRUE, 1, 'Thank you for your feedback!', NOW());
 *   ```
 *   查询数据示例
 *   查看某用户的主题帖和评论：
 *   ```sql
 *   SELECT * FROM feedback
 *   WHERE openid = 'user12345' OR
 *         (parent_id IS NOT NULL AND parent_id IN (SELECT id FROM feedback WHERE openid = 'user12345'));
 *   ```
 *   管理员查看所有数据：
 *   ```sql
 *   SELECT * FROM feedback;
 *   ```
 */
interface FeedbackMapper {

    // 管理员查看所有数据
    @Select("SELECT * FROM feedback")
    fun getAllFeedbacks(): List<FeedbackModel>?

    // 任何人创建一个主题贴/回复某个主题贴
    @Insert("INSERT INTO feedback (openid, is_admin, parent_id, content, created_at) VALUES (#{openid}, #{isAdmin}, #{parent_id}, #{content}, NOW())")
    fun createFeedback(feedbackModel: FeedbackModel): Int

    // 用户查看自己的帖子和评论
    @Select("SELECT * FROM feedback WHERE openid = #{openid} OR (parent_id IS NOT NULL AND parent_id IN (SELECT id FROM feedback WHERE openid = #{openid}))")
    fun getFeedbacksByOpenid(openid: String): List<FeedbackModel>?
}
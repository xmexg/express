package org.looom.wkexpress_web.model

import lombok.Setter
import java.sql.Timestamp

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
class FeedbackModel {

    var id: Int? = null
    @Setter
    var openid: String? = null
    @Setter
    var isAdmin: Boolean? = null
    @Setter
    var parent_id: Int? = null
    @Setter
    var content: String? = null
    @Setter
    var created_at: Timestamp? = null
    @Setter
    var updated_at: Timestamp? = null
}
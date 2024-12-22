package org.looom.wkexpress_web.server

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONArray
import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.JSONWriter
import org.looom.wkexpress_web.entity.User
import org.looom.wkexpress_web.mapper.FeedbackMapper
import org.looom.wkexpress_web.model.FeedbackModel
import org.looom.wkexpress_web.util.MyBatisUtils
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
class FeedbackServer {
    companion object{
        val feedbackMapper = MyBatisUtils.getSession().getMapper(FeedbackMapper::class.java)

        fun createFeedback(token: String?, parent_id: Int?, content: String?): Boolean {
            if (token == null || content == null) return false
            val user: User = UserServer.getUser(token) ?: return false
            println("获取到的用户信息: "+ JSON.toJSONString(user))
            val feedback = FeedbackModel().apply {
                openid = user.user_openid
                isAdmin = user.user_type == 9
                this.parent_id = parent_id
                this.content = content
                this.created_at = Timestamp(System.currentTimeMillis())
            }
            print("生成数据包: "+ JSON.toJSONString(feedback))
            return feedbackMapper.createFeedback(feedback) > 0
        }

        fun getAllFeedbacks(): JSONObject {
            val allFeedbacks: List<FeedbackModel> = feedbackMapper.getAllFeedbacks() ?: return JSONObject()
            return conv(allFeedbacks)
        }

        fun getFeedbacksByOpenid(user_openid: String?): JSONObject {
            if (user_openid == null) return JSONObject()
            val allFeedbacks: List<FeedbackModel> = feedbackMapper.getFeedbacksByOpenid(user_openid) ?: return JSONObject()
            return conv(allFeedbacks)
        }

        /**
         * 转换器, 用于从list转换成前端识别的
         * {
         *   "feedbacks": [
         *     {
         *       "id": 1,
         *       "openid": "41002",
         *       "isAdmin": false,
         *       "parent_id": null,
         *       "content": "主题帖",
         *       "created_at": 1734855338537,
         *       "updated_at": null,
         *       "children": [
         *         {
         *           "id": 3,
         *           "openid": "41002",
         *           "isAdmin": false,
         *           "parent_id": 1,
         *           "content": "主题帖的子贴",
         *           "created_at": 1734855338538,
         *           "updated_at": null,
         *           "children": []
         *         }
         *       ]
         *     }
         *   ]
         * }
         *
         */
        private fun conv(allFeedbacks : List<FeedbackModel>): JSONObject{

            // 用来存储最终结果的 JSON 数组
            val resultArray = JSONArray()

            // 先按照 parent_id 分类，方便后续组织主题帖和子帖
            val feedbackMap = mutableMapOf<Int, MutableList<FeedbackModel>>()
            val rootFeedbacks = mutableListOf<FeedbackModel>()

            // 遍历所有反馈，按照 parent_id 分类
            allFeedbacks.forEach { feedback ->
                if (feedback.parent_id == null) {
                    // parent_id 为 null 的是主题帖
                    rootFeedbacks.add(feedback)
                } else {
                    // parent_id 不为 null 的是子帖，按 parent_id 分类
                    feedbackMap.computeIfAbsent(feedback.parent_id!!) { mutableListOf() }.add(feedback)
                }
            }

            // 将主题帖和子帖构建成所需格式
            rootFeedbacks.forEach { rootFeedback ->
                val jsonObject = JSONObject()

                // 将每个属性转换为 JSON 字段
                jsonObject.put("id", rootFeedback.id)
                jsonObject.put("openid", rootFeedback.openid)
                jsonObject.put("isAdmin", rootFeedback.isAdmin)
                jsonObject.put("parent_id", rootFeedback.parent_id)
                jsonObject.put("content", rootFeedback.content)
                jsonObject.put("created_at", rootFeedback.created_at?.time)
                jsonObject.put("updated_at", rootFeedback.updated_at?.time)

                // 查找并添加子帖
                val children = JSONArray()
                feedbackMap[rootFeedback.id]?.forEach { childFeedback ->
                    val childObject = JSONObject()
                    childObject.put("id", childFeedback.id)
                    childObject.put("openid", childFeedback.openid)
                    childObject.put("isAdmin", childFeedback.isAdmin)
                    childObject.put("parent_id", childFeedback.parent_id)
                    childObject.put("content", childFeedback.content)
                    childObject.put("created_at", childFeedback.created_at?.time)
                    childObject.put("updated_at", childFeedback.updated_at?.time)

                    // 查找并添加子帖的子帖
                    val childChildren = JSONArray()
                    feedbackMap[childFeedback.id]?.forEach { grandChildFeedback ->
                        val grandChildObject = JSONObject()
                        grandChildObject.put("id", grandChildFeedback.id)
                        grandChildObject.put("openid", grandChildFeedback.openid)
                        grandChildObject.put("isAdmin", grandChildFeedback.isAdmin)
                        grandChildObject.put("parent_id", grandChildFeedback.parent_id)
                        grandChildObject.put("content", grandChildFeedback.content)
                        grandChildObject.put("created_at", grandChildFeedback.created_at?.time)
                        grandChildObject.put("updated_at", grandChildFeedback.updated_at?.time)
                        childChildren.add(grandChildObject)
                    }

                    // 将子帖的子帖添加到子帖的 "children" 字段
                    childObject.put("children", childChildren)

                    // 将当前子帖添加到子帖列表
                    children.add(childObject)
                }

                // 将子帖添加到当前主题帖的 "children" 字段
                jsonObject.put("children", children)

                // 将当前主题帖添加到结果数组
                resultArray.add(jsonObject)
            }

            // 返回最终的 JSON 对象，包含所有主题帖及其子帖
            val finalResult = JSONObject()
            finalResult.put("feedbacks", resultArray)

            // 使用 Fastjson2 的 JSONWriter.Feature.NullAsDefaultValue 确保 null 值字段被保留
            val jsonString = finalResult.toJSONString(JSONWriter.Feature.NullAsDefaultValue)

            // 这里返回的最终 JSON 字符串，你可以根据需要返回 JSONObject
            return JSONObject.parseObject(jsonString)
        }
    }
}
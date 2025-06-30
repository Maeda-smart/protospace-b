package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.CommentNotificationEntity;

@Mapper
public interface CommentNotificationRepository {
  @Select("SELECT c.id AS commentId, c.text AS commentText, c.created_at AS createdAt, p.id AS prototypeId, p.prototypename AS prototypeName, u.id AS commenterId, u.nickname AS commenterName FROM comments c JOIN prototype p ON c.prototype_id = p.id JOIN users u ON c.user_id = u.id WHERE p.user_id = #{userId} AND c.user_id != #{userId} ORDER BY c.created_at DESC")
    List<CommentNotificationEntity> findCommentNotificationByUserId(@Param("userId") Integer userId);
}
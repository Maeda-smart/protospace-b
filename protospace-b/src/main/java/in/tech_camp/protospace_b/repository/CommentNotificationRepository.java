package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.protospace_b.entity.CommentNotificationEntity;

@Mapper
public interface CommentNotificationRepository {

    @Select("""
                SELECT
                    n.id,
                    n.comment_id,
                    n.prototype_id AS prototypeId,
                    n.recipient_user_id,
                    n.commenter_user_id,
                    n.is_read,
                    n.created_at,
                    p.prototypeName AS prototypeName,
                    u.nickname AS commenterName
                FROM notifications n
                JOIN prototype p ON n.prototype_id = p.id
                JOIN users u ON n.commenter_user_id = u.id
                WHERE n.recipient_user_id = #{userId}
                  AND n.is_read = FALSE
                ORDER BY n.created_at DESC
            """)
    List<CommentNotificationEntity> findUnreadByRecipientId(@Param("userId") Integer userId);

    @Update("UPDATE notifications SET is_read = TRUE WHERE id = #{id}")
    void markAsRead(@Param("id") Integer notificationId);

    @Insert("""
                INSERT INTO notifications (
                    comment_id,
                    prototype_id,
                    recipient_user_id,
                    commenter_user_id,
                    is_read,
                    created_at
                )
                VALUES (
                    #{commentId},
                    #{prototypeId},
                    #{recipientUserId},
                    #{commenterUserId},
                    FALSE,
                    NOW()
                )
            """)
    void insertNotification(CommentNotificationEntity notification);
}
package in.tech_camp.protospace_b.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommentNotificationEntity {
  private Integer id;
  private Integer commentId;
  private LocalDateTime createdAt;
  private Integer prototypeId;
  private String prototypeName;
  private Integer commenterUserId;
  private String commenterName;
  private Integer recipientUserId;
  private boolean isRead;
}

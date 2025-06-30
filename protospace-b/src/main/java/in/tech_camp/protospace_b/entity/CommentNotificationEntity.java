package in.tech_camp.protospace_b.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommentNotificationEntity {
  private Integer commentId;
  private String commentText;
  private LocalDateTime createdAt;
  private Integer prototypeId;
  private String prototypeName;
  private Integer commenterId;
  private String commenterName;
}

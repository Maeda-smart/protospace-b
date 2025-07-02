package in.tech_camp.protospace_b.entity;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class MessageEntity {
  private Integer id;
  private String content;
  private UserEntity user;
  private RoomEntity room;
  private String image;
  private Timestamp createdAt;
}

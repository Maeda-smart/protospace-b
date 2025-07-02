package in.tech_camp.protospace_b.entity;

import lombok.Data;

@Data
public class RoomUserEntity {
  private Long id;
  private UserEntity user;
  private RoomEntity room;
}

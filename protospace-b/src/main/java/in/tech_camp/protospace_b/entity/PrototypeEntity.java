package in.tech_camp.protospace_b.entity;

import lombok.Data;

@Data
public class PrototypeEntity {
  private Integer id;
  private String prototypeName;
  private String catchCopy;
  private String concept;
  private String imgPath;
  private UserEntity user;

  // いいね数集計
  private int niceCount;
}

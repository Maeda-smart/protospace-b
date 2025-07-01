package in.tech_camp.protospace_b.entity;

import lombok.Data;

@Data
public class BookmarkEntity {

    private UserEntity user;

    private PrototypeEntity prototype;
}

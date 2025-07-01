package in.tech_camp.protospace_b.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommentEntity {
    private Integer id;
    private String text;
    private UserEntity user;
    private PrototypeEntity prototype;
    private LocalDateTime createdAt;
}

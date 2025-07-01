package in.tech_camp.protospace_b.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReadStatusEntity {
    private Integer prototypeId;
    private Integer userId;
    private LocalDateTime readAt;
}

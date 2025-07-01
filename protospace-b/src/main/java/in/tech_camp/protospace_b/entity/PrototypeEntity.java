package in.tech_camp.protospace_b.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class PrototypeEntity {
    private Integer id;
    private String prototypeName;
    private String catchCopy;
    private String concept;
    private String imgPath;
    private UserEntity user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TagEntity> tags;
    private TagEntity tag;
    private Integer niceCount;
    private boolean isNice;
    private boolean read;
    private boolean bookmark;
    private boolean pin;
}

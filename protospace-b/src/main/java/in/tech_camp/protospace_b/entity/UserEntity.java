package in.tech_camp.protospace_b.entity;

import lombok.Data;

@Data
public class UserEntity {
    private Integer id;
    private String nickname;
    private String email;
    private String password;
    private String profile;
    private String affiliation;
    private String position;
    // "ROLE_USER"|"ROLE_MODERATOR"|"ROLE_ADMIN"
    private String roleName;
    private boolean enable;
}

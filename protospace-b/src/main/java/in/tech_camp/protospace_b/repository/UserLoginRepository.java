package in.tech_camp.protospace_b.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.UserEntity;

@Mapper
public interface UserLoginRepository {
    @Select("""
    SELECT id, nickname, email, password, profile,
           affiliation, position, role_name, is_enable
    FROM users
    WHERE email = #{email}
    """)
    @Results(id = "userResultMap", value = {
        @Result(property = "id", column = "id"),
        @Result(property = "nickname", column = "nickname"),
        @Result(property = "email", column = "email"),
        @Result(property = "password", column = "password"),
        @Result(property = "profile", column = "profile"),
        @Result(property = "affiliation", column = "affiliation"),
        @Result(property = "position", column = "position"),
        @Result(property = "roleName", column = "role_name"),
        @Result(property = "enable", column = "enable")
    })
    UserEntity findByEmail(String email);
}
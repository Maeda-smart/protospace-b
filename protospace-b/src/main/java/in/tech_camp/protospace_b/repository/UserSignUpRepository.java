package in.tech_camp.protospace_b.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.UserEntity;

@Mapper
public interface UserSignUpRepository {
    @Insert("INSERT INTO users(nickname, email, password, profile, affiliation, position, role_name) VALUES (#{nickname}, #{email}, #{password}, #{profile}, #{affiliation}, #{position}, #{roleName})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    void insert(UserEntity user);

    @Select("SELECT EXISTS(SELECT 1 FROM users WHERE email = #{email})")
    boolean existsByEmail(String email);
}

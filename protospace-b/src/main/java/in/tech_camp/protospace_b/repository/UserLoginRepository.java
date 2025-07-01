package in.tech_camp.protospace_b.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.UserEntity;

@Mapper
public interface UserLoginRepository {
    @Select("SELECT * FROM users WHERE email = #{email}")
    @Result(property = "roleName", column = "role_name")
    UserEntity findByEmail(String email);
}
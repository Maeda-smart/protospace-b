package in.tech_camp.protospace_b.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.UserEntity;

@Mapper
public interface UserDetailRepository {
    // ユーザーidでユーザー情報を取得
    @Select("SELECT id, nickname, email, profile, affiliation, position, role_name, enable FROM users WHERE id = #{id}")
    @Result(property = "roleName", column = "role_name")
    UserEntity findById(Integer id);
}

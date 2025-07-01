package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.protospace_b.entity.UserEntity;

@Mapper
public interface AdminUserRepository {
    // 全ユーザー情報を取得
    @Select("SELECT id, nickname, email, password, profile, affiliation, position, role_name, enable FROM users")
    List<UserEntity> findAllUsers();

    // 指定のユーザーをidで検索
    @Select("SELECT * FROM users WHERE id = #{id}")
    UserEntity findById(Integer id);

    // 指定のユーザーのenableをfalseに更新（凍結）
    @Update("UPDATE users SET enable = false WHERE id = #{id}")
    void freezeUserById(Integer id);
}

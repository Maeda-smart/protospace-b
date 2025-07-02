package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.protospace_b.entity.UserEntity;

@Mapper
public interface AdminUserRepository {
    // 全ユーザー情報を取得
    @Select("""
        SELECT id, nickname, email, password, profile, affiliation, position, role_name, enable
        FROM users
        WHERE id != #{loginUserId}
        ORDER BY id ASC
    """)
    @Result(property = "roleName", column = "role_name")
    List<UserEntity> findAllUsers(Integer loginUserId);

    // 指定のユーザーをidで検索
    @Select("SELECT * FROM users WHERE id = #{id}")
    UserEntity findById(Integer id);

    // 指定のユーザーのenableをfalseに更新（凍結）
    @Update("UPDATE users SET enable = false WHERE id = #{id}")
    void freezeUserById(Integer id);

    // 指定のユーザーのenableをtrueに更新（凍結解除）
    @Update("UPDATE users SET enable = true WHERE id = #{id}")
    void unfreezeUserById(Integer id);

    // 指定のユーザーのRoleを更新(モデレーターに昇格)
    @Update("UPDATE users SET role_name = 'ROLE_MODERATOR' where id = #{id}")
    void promoteUserById(Integer id);

    // 指定のユーザーのRoleを更新(一般ユーザーに降格)
    @Update("UPDATE users SET role_name = 'ROLE_USER' where id = #{id}")
    void demoteUserById(Integer id);
}

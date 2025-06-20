package in.tech_camp.protospace_b.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.UserEntity;

@Mapper
public interface UserDetailRepository {
  // ユーザーidでユーザー情報を取得
  @Select("SELECT id, nickname, email, profile, affiliation, position FROM users WHERE id = #{id}")
  UserEntity findById(Integer id);
}

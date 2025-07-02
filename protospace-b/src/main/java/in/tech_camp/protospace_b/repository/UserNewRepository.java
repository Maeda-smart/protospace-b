package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.UserEntity;

@Mapper
public interface UserNewRepository {
  @Select("select * from users where id <> #{excludedId}")
  List<UserEntity> findAllExcept(Integer excludedId);

  // ユーザーidでユーザー情報を取得
  @Select("SELECT * FROM users WHERE id = #{id}")
  UserEntity findById(Integer id);
}
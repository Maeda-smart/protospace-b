package in.tech_camp.protospace_b.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.protospace_b.entity.UserEntity;

@Mapper
public interface UserEditRepository {
  @Select("SELECT * FROM users WHERE id = #{id}")
  UserEntity findById(Integer id);

  @Update("UPDATE users SET nickname = #{nickname}, email = #{email}, password = #{password}, profile = #{profile}, affiliation = #{affiliation}, position = #{position} WHERE id = #{id}")
  void update(UserEntity user);

  @Select("SELECT COUNT(*) > 0 FROM users WHERE email = #{email} AND id != #{userId}")
  boolean existsByEmailExcludingCurrent(String email, Integer userId);
}

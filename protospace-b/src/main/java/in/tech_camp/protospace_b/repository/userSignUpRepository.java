package in.tech_camp.protospace_b.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

import in.tech_camp.protospace_b.entity.UserEntity;

public interface userSignUpRepository {
  @Insert("INSERT INTO users(nickname, email, password, profile, affiliation, position)")
  @Options(useGeneratedKeys=true, keyProperty="id")
  void insert(UserEntity user);
}

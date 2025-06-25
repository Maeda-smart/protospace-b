package in.tech_camp.protospace_b.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import in.tech_camp.protospace_b.entity.NiceEntity;

@Mapper
public interface NiceRepository {

  // いいねの保存
  @Insert("INSERT INTO nice (prototype_id, user_id) VALUES (#{prototype.id}, #{user.id})")
  void insert(NiceEntity nice);
}

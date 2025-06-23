package in.tech_camp.protospace_b.repository;

import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Options;

import in.tech_camp.protospace_b.entity.PrototypeEntity;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PrototypeEditRepository {
  @Update("UPDATE prototype SET prototypeName = #{prototypeName}, catchCopy = #{catchCopy}, concept = #{concept}, img = #{imgPath}, user_id = #{user.id} WHERE id = #{id}")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void update(PrototypeEntity prototype);
}
package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.PrototypeEntity;

@Mapper
public interface PrototypeShowRepository {
  @Select("SELECT * FROM prototype")
  List<PrototypeEntity> showAll();

  @Select("SELECT * FROM prototype WHERE user_id = #{userId}")
  List<PrototypeEntity> showByUserId(Integer userId);
}

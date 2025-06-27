package in.tech_camp.protospace_b.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PrototypeDeleteRepository {
  @Delete("DELETE FROM prototype WHERE id = #{id}")
  void deleteById(Integer id);
}

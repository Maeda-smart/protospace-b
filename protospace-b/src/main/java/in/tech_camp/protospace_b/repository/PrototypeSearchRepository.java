package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.PrototypeEntity;

@Mapper
public interface PrototypeSearchRepository {
  // OPTIMIZE: N+1
  @Select("SELECT * FROM prototype WHERE prototypeName LIKE CONCAT('%', #{prototypeName}, '%')")
  @Results(value = {
      @Result(property = "imgPath", column = "img"),
      @Result(property = "user", column = "user_id", one = @One(select = "in.tech_camp.protospace_b.repository.UserNewRepository.findById"))
  })
  List<PrototypeEntity> findByPrototypeName(String prototypeName);
}

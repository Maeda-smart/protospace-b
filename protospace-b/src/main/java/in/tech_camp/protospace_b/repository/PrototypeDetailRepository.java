package in.tech_camp.protospace_b.repository;

import in.tech_camp.protospace_b.entity.PrototypeEntity;
// import in.tech_camp.protospace_b.entity.UserEntity;
import org.apache.ibatis.annotations.*;

// import java.util.List;

@Mapper
public interface PrototypeDetailRepository {
  @Select("SELECT * FROM prototype WHERE id = #{Id}")
  @Results(value = {
    @Result(property = "id", column = "id"),
    @Result(property = "prototypeName", column = "prototypename"),
    @Result(property = "catchCopy", column = "catchcopy"),
    @Result(property = "concept", column = "concept"),
    @Result(property = "imgPath", column = "img"),
    @Result(property = "user", column = "user_id",
            one = @One(select = "in.tech_camp.protospace_b.repository.UserNewRepository.findById"))
  })
  PrototypeEntity findByPrototypeId(Integer id);
}


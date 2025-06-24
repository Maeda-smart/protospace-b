package in.tech_camp.protospace_b.repository;

import org.apache.ibatis.annotations.*;

import in.tech_camp.protospace_b.entity.PrototypePinEntity;
import java.util.List;

@Mapper
public interface PrototypePinRepository {
  @Select("SELECT * FROM prototype_pin WHERE user_id = #{userId}")
  List<PrototypePinEntity> findByUserId(Integer userId);
}

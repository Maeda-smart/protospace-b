package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.PrototypeEntity;

@Mapper
public interface PrototypeShowRepository {
  @Select("SELECT * FROM prototype")
  @Results(value = {
      @Result(property = "id", column = "id"),
      @Result(property = "user", column = "user_id", many = @Many(select = "in.tech_camp.protospace_b.repository.UserDetailRepository.findById")),
      @Result(property = "imgPath", column = "img")
  })
  List<PrototypeEntity> showAll();

  @Select("SELECT * FROM prototype WHERE user_id = #{userId}")
  @Results(value = {
      @Result(property = "imgPath", column = "img")
  })
  List<PrototypeEntity> showByUserId(Integer userId);
}

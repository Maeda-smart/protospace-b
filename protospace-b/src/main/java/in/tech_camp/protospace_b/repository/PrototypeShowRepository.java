package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.PrototypeEntity;

@Mapper
public interface PrototypeShowRepository {
  String SELECTOR = """
      SELECT
        p.id p_id,
        p.prototypeName,
        p.catchCopy,
        p.concept,
        p.img,
        p.user_id
      FROM
        prototype p
      """;
  @Select(SELECTOR)
  @Results(value = {
      @Result(property = "id", column = "p_id"),
      @Result(property = "user", column = "user_id", one = @One(select = "in.tech_camp.protospace_b.repository.UserDetailRepository.findById")),
      @Result(property = "imgPath", column = "img"),
      @Result(property="tags", column="p_id", many = @Many(select="in.tech_camp.protospace_b.repository.TagRepository.prototypeTags"))
  })
  List<PrototypeEntity> showAll();

  @Select(SELECTOR + " WHERE user_id = #{userId}")
  @Results(value = {
      @Result(property = "id", column = "p_id"),
      @Result(property = "user", column = "user_id", one = @One(select = "in.tech_camp.protospace_b.repository.UserDetailRepository.findById")),
      @Result(property = "imgPath", column = "img"),
      @Result(property="tags", column="p_id", many = @Many(select="in.tech_camp.protospace_b.repository.TagRepository.prototypeTags"))
  })
  List<PrototypeEntity> showByUserId(Integer userId);
}

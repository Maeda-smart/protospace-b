package in.tech_camp.protospace_b.repository;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.PrototypeEntity;

@Mapper
public interface PrototypeDetailRepository {
  @Select("SELECT p.id, p.prototypeName, p.catchCopy, p.concept, p.img, p.user_id, t.id FROM prototype p LEFT JOIN prototype_tags pt ON p.id = pt.prototype_id LEFT JOIN tags t ON pt.tags_id = t.id WHERE p.id = #{id}")
  @Results(value = {
      @Result(property = "id", column = "p.id"),
      @Result(property = "imgPath", column = "img"),
      @Result(property = "user", column = "user_id", one = @One(select = "in.tech_camp.protospace_b.repository.UserNewRepository.findById")),
      @Result(property="tags", column="t.id", many = @Many(select="in.tech_camp.protospace_b.repository.TagRepository."))
  })
  PrototypeEntity findByPrototypeId(Integer id);
}

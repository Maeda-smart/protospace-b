package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.PrototypeEntity;

@Mapper
public interface PrototypeSearchRepository {
  // OPTIMIZE: N+1
  @Select("""
      SELECT
        p.id p_id,
        p.prototypeName prototypeName,
        p.catchCopy,
        p.concept,
        p.img,
        u.id u_id,
        u.nickname nickname
      FROM
        prototype p
      LEFT JOIN users u ON p.user_id = u.id
      WHERE
        prototypeName LIKE CONCAT('%', #{prototypeName}, '%')
      """)
  @Results(value = {
      @Result(property = "id", column = "p_id"),
      @Result(property = "imgPath", column = "img"),
      @Result(property="createdAt", column="created_at"),
      @Result(property="updatedAt", column="updated_at"),
      @Result(property = "user.id", column = "u_id"),
      @Result(property = "user.nickname", column = "nickname"),
      @Result(property = "tags", column = "p_id", many = @Many(select = "in.tech_camp.protospace_b.repository.TagRepository.prototypeTags"))
  })
  List<PrototypeEntity> findByPrototypeName(String prototypeName);
}

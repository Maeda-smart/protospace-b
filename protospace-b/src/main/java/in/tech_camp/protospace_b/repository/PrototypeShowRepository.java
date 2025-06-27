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
        u.id u_id,
        u.nickname nickname
      FROM
        prototype p
      LEFT JOIN users u ON p.user_id = u.id
      """;

  // OPTIMIZE: N+1
  @Select(SELECTOR)
  @Results(value = {
      @Result(property = "id", column = "p_id"),
      @Result(property = "user.id", column = "u_id"),
      @Result(property = "user.nickname", column = "nickname"),
      @Result(property = "imgPath", column = "img"),
      @Result(property = "tags", column = "p_id", many = @Many(select = "in.tech_camp.protospace_b.repository.TagRepository.prototypeTags"))
  })
  List<PrototypeEntity> showAll();

  // OPTIMIZE: N+1
  @Select(SELECTOR + " WHERE user_id = #{userId}")
  @Results(value = {
      @Result(property = "id", column = "p_id"),
      @Result(property = "user.id", column = "u_id"),
      @Result(property = "user.nickname", column = "nickname"),
      @Result(property = "imgPath", column = "img"),
      @Result(property = "tags", column = "p_id", many = @Many(select = "in.tech_camp.protospace_b.repository.TagRepository.prototypeTags"))
  })
  List<PrototypeEntity> showByUserId(Integer userId);

  @Select(SELECTOR + " WHERE p.id = #{id}")
  @Results(value = {
      @Result(property = "id", column = "p_id"),
      @Result(property = "user", column = "u_id", one = @One(select = "in.tech_camp.protospace_b.repository.UserDetailRepository.findById")),
      @Result(property = "imgPath", column = "img"),
      @Result(property = "tags", column = "p_id", many = @Many(select = "in.tech_camp.protospace_b.repository.TagRepository.prototypeTags"))
  })
  PrototypeEntity findByPrototypeId(Integer id);
}

package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.PrototypeEntity;

@Mapper
public interface PrototypeShowRepository {

  // OPTIMIZE: N+1
  @Select("""
      SELECT
        p.id p_id,
        p.prototypeName,
        p.catchCopy,
        p.concept,
        p.img,
        u.id u_id,
        u.nickname nickname,
        COALESCE(n.niceCount, 0) niceCount,
        MAX(CASE WHEN n.user_id = #{currentUserId} THEN 1 ELSE 0 END) isNice,
        MAX(CASE WHEN r.user_id = #{currentUserId} THEN 1 ELSE 0 END) read
      FROM
        prototype p
      LEFT JOIN users u ON p.user_id = u.id
      LEFT JOIN (
        SELECT 
          n_summary.user_id,
          n_summary.prototype_id,
          COUNT(*) niceCount
        FROM
          nice n_summary
        GROUP BY
          n_summary.user_id,
          n_summary.prototype_id
      ) n ON p.id = n.prototype_id
      LEFT JOIN prototype_read_status r ON r.prototype_id = p.id AND r.user_id = #{currentUserId}
      LEFT JOIN prototype_tags pt ON p.id = pt.prototype_id
      GROUP BY
        p.id,
        p.prototypeName,
        p.catchCopy,
        p.concept,
        p.img,
        u.id,
        u.nickname,
        n.niceCount
      """)
  @Results(value = {
      @Result(property = "id", column = "p_id"),
      @Result(property = "user.id", column = "u_id"),
      @Result(property = "user.nickname", column = "nickname"),
      @Result(property = "imgPath", column = "img"),
      @Result(property = "tags", column = "p_id", many=@Many(select = "in.tech_camp.protospace_b.repository.TagRepository.prototypeTags"))
  })
  List<PrototypeEntity> showAll(Integer currentUserId);

  // OPTIMIZE: N+1
  @Select("""
      SELECT
        p.id p_id,
        p.prototypeName,
        p.catchCopy,
        p.concept,
        p.img,
        u.id u_id,
        u.nickname nickname,
        COALESCE(n.niceCount, 0) niceCount,
        MAX(CASE WHEN n.user_id = #{currentUserId} THEN 1 ELSE 0 END) isNice,
        MAX(CASE WHEN r.user_id = #{currentUserId} THEN 1 ELSE 0 END) read,
        MAX(CASE WHEN pin.user_id = #{userId} THEN 1 ELSE 0 END) pinned
      FROM
        prototype p
      LEFT JOIN users u ON p.user_id = u.id AND p.user_id = #{userId}
      LEFT JOIN (
        SELECT 
          n_summary.user_id,
          n_summary.prototype_id,
          COUNT(*) niceCount
        FROM
          nice n_summary
        GROUP BY
          n_summary.user_id,
          n_summary.prototype_id
      ) n ON p.id = n.prototype_id
      LEFT JOIN prototype_read_status r ON r.prototype_id = p.id AND r.user_id = #{currentUserId}
      LEFT JOIN prototype_tags pt ON p.id = pt.prototype_id
      LEFT JOIN pin ON p.id = pin.prototype_id
      GROUP BY
        p.id,
        p.prototypeName,
        p.catchCopy,
        p.concept,
        p.img,
        u.id,
        u.nickname,
        n.niceCount
      """)
  @Results(value = {
      @Result(property = "id", column = "p_id"),
      @Result(property = "user.id", column = "u_id"),
      @Result(property = "user.nickname", column = "nickname"),
      @Result(property = "imgPath", column = "img"),
      @Result(property = "tags", column = "p_id", many = @Many(select = "in.tech_camp.protospace_b.repository.TagRepository.prototypeTags")),
      @Result(property = "pin", column = "pinned"),
  })
  List<PrototypeEntity> showByUserId(Integer currentUserId, Integer userId);

  @Select("""
      SELECT
        p.id p_id,
        p.prototypeName,
        p.catchCopy,
        p.concept,
        p.img,
        p.user_id u_id,
        COALESCE(n.niceCount, 0) niceCount,
        MAX(CASE WHEN n.user_id = #{currentUserId} THEN 1 ELSE 0 END) isNice,
        MAX(CASE WHEN r.user_id = #{currentUserId} THEN 1 ELSE 0 END) read
      FROM
        prototype p
      LEFT JOIN (
        SELECT 
          n_summary.user_id,
          n_summary.prototype_id,
          COUNT(*) niceCount
        FROM
          nice n_summary
        GROUP BY
          n_summary.user_id,
          n_summary.prototype_id
      ) n ON p.id = n.prototype_id AND p.id = #{id}
      LEFT JOIN prototype_read_status r ON r.prototype_id = p.id AND r.user_id = #{currentUserId}
      LEFT JOIN prototype_tags pt ON p.id = pt.prototype_id
      LEFT JOIN bookmark b ON p.id = b.prototype_id AND b.user_id = #{currentUserId}
      LEFT JOIN pin ON p.id = pin.prototype_id
      WHERE p.id = #{id}
      GROUP BY
        p.id,
        p.prototypeName,
        p.catchCopy,
        p.concept,
        p.img,
        p.user_id,
        n.niceCount
      """)
  @Results(value = {
      @Result(property = "id", column = "p_id"),
      @Result(property = "user", column = "u_id", one = @One(select = "in.tech_camp.protospace_b.repository.UserDetailRepository.findById")),
      @Result(property = "imgPath", column = "img"),
      @Result(property = "tags", column = "p_id", many = @Many(select = "in.tech_camp.protospace_b.repository.TagRepository.prototypeTags"))
  })
  PrototypeEntity findByPrototypeId(Integer currentUserId, Integer id);

  @Select("""
      SELECT
        p.id p_id,
        p.prototypeName,
        p.catchCopy,
        p.concept,
        p.img,
        u.id u_id,
        u.nickname nickname,
        COALESCE(n.niceCount, 0) niceCount,
        MAX(CASE WHEN n.user_id = #{currentUserId} THEN 1 ELSE 0 END) isNice,
        MAX(CASE WHEN r.user_id = #{currentUserId} THEN 1 ELSE 0 END) read
      FROM
        prototype p
      LEFT JOIN users u ON p.user_id = u.id AND p.prototypeName LIKE CONCAT('%', #{prototypeName}, '%')
      LEFT JOIN (
        SELECT 
          n_summary.user_id,
          n_summary.prototype_id,
          COUNT(*) niceCount
        FROM
          nice n_summary
        GROUP BY
          n_summary.user_id,
          n_summary.prototype_id
      ) n ON p.id = n.prototype_id
      LEFT JOIN prototype_read_status r ON r.prototype_id = p.id AND r.user_id = #{currentUserId}
      LEFT JOIN prototype_tags pt ON p.id = pt.prototype_id
      LEFT JOIN pin ON p.id = pin.prototype_id
      GROUP BY
        p.id,
        p.prototypeName,
        p.catchCopy,
        p.concept,
        p.img,
        u.id,
        u.nickname,
        n.niceCount
      """)
  @Results(value = {
      @Result(property = "id", column = "p_id"),
      @Result(property = "user.id", column = "u_id"),
      @Result(property = "user.nickname", column = "nickname"),
      @Result(property = "imgPath", column = "img"),
      @Result(property = "tags", column = "p_id", many = @Many(select = "in.tech_camp.protospace_b.repository.TagRepository.prototypeTags")),
      @Result(property = "pin", column = "pinned"),
  })
  List<PrototypeEntity> findByPrototypeName(@Param("currentUserId") Integer currentUserId, @Param("prototypeName") String prototypeName);
}

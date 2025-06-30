package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.NiceEntity;
import in.tech_camp.protospace_b.entity.PrototypeEntity;

@Mapper
public interface NiceRepository {

  // いいねの保存
  @Insert("INSERT INTO nice (prototype_id, user_id) VALUES (#{prototype.id}, #{user.id})")
  void insert(NiceEntity nice);

  // いいね済みかを判定
  @Select("SELECT COUNT(*) > 0 FROM nice WHERE prototype_id = #{prototypeId} AND user_id = #{userId}")
  boolean existNice(@Param("prototypeId") Integer prototypeId, @Param("userId") Integer userId);

  // いいねの削除
  @Delete("DELETE FROM nice WHERE prototype_id = #{prototypeId} AND user_id = #{userId}")
  void deleteNice(@Param("prototypeId") Integer prototypeId, @Param("userId") Integer userId);

  // プロトタイプごとのいいね数を取得
  @Select("SELECT COUNT(*) FROM nice WHERE prototype_id = #{prototype.id}")
  int countNiceByPrototypeId(Integer prototypeId);

  // プロトタイプごとのいいね数を取得し、多い順に並び替えたうえで全プロトタイプ取得
  // OPTIMIZE: N+1
  @Select("""
      SELECT
        p.id p_id,
        p.prototypeName,
        p.catchCopy,
        p.concept,
        p.img,
        p.created_at,
        p.updated_at,
        u.id u_id,
        u.nickname nickname,
        COUNT(n.user_id) AS nice_count
      FROM
        prototype p
      LEFT JOIN users u ON u.id = p.user_id
      LEFT JOIN nice n ON p.id = n.prototype_id
      GROUP BY
        p.id,
        p.prototypeName,
        p.catchCopy,
        p.concept,
        p.img,
        p.created_at,
        p.updated_at,
        u.id,
        u.nickname
      ORDER BY nice_count DESC
      """)
  @Results(value = {
      @Result(property = "id", column = "p_id"),
      @Result(property = "user.id", column = "u_id"),
      @Result(property = "user.nickname", column = "nickname"),
      @Result(property = "imgPath", column = "img"),
      @Result(property = "createdAt", column="created_at"),
      @Result(property = "updatedAt", column="updated_at"),
      @Result(property = "tags", column = "p_id", many = @Many(select = "in.tech_camp.protospace_b.repository.TagRepository.prototypeTags"))
  })
  List<PrototypeEntity> findPrototypesOrderByCountDesc();
}

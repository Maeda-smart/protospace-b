package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
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
  boolean existNice(@Param("prototypeId") Integer prototypeId,@Param("userId") Integer userId);

  // いいねの削除
  @Delete("DELETE FROM nice WHERE prototype_id = #{prototypeId} AND user_id = #{userId}")
  void deleteNice(@Param("prototypeId") Integer prototypeId,@Param("userId") Integer userId);

  // プロトタイプごとのいいね数を取得
  @Select("SELECT COUNT(*) FROM nice WHERE prototype_id = #{prototype.id}")
  int countNiceByPrototypeId(Integer prototypeId);

  // プロトタイプごとのいいね数を取得し、多い順に並び替えたうえで全プロトタイプ取得
  @Select("SELECT p.*, COUNT(n.user_id) as nice_count FROM prototype p " +
          "LEFT JOIN nice n ON p.id = n.prototype_id " +
          "GROUP BY p.id ORDER BY nice_count DESC")
  @Results(value = {
    @Result(property = "id", column = "id"),
        @Result(property = "prototypeName", column = "prototypename"),
        @Result(property = "catchCopy", column = "catchcopy"),
        @Result(property = "concept", column = "concept"),
        @Result(property = "imgPath", column = "img"),
        @Result(property = "user", column = "user_id",
                one = @One(select = "in.tech_camp.protospace_b.repository.UserNewRepository.findById"))
  })
  List<PrototypeEntity> findPrototypesOrderByCountDesc();
}

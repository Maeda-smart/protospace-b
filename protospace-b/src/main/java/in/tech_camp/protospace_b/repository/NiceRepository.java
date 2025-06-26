package in.tech_camp.protospace_b.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.NiceEntity;

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
}

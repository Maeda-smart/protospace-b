package in.tech_camp.protospace_b.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.NiceEntity;

@Mapper
public interface NiceRepository {

  // いいねの保存
  @Insert("INSERT INTO nice (prototype_id, user_id) VALUES (#{prototype.id}, #{user.id})")
  void insert(NiceEntity nice);

  // いいね済みかを判定
  @Select("SELECT COUNT(*) > 0 FROM nice WHERE prototype_id = #{prototypeId} AND user_id = #{userId}")
  boolean existNice(Integer prototypeId, Integer userId);

  // いいねの削除
  @Delete("DELETE FROM nice WHERE prototype_id = #{prototypeId} AND user_id = #{userId}")
  void deleteNice(Integer prototypeId, Integer userId);
}

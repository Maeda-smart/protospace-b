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

import in.tech_camp.protospace_b.entity.BookmarkEntity;
import in.tech_camp.protospace_b.entity.PrototypeEntity;

@Mapper
public interface BookmarkRepository {

  // ブックマークの保存
  @Insert("INSERT INTO bookmark (user_id, prototype_id) VALUES (#{user.id}, #{prototype.id})")
  void insert(BookmarkEntity bookmark);

  // ブックマークした投稿の一覧表示
  @Select("SELECT * FROM prototype p INNER JOIN bookmark b ON p.id = b.prototype_id WHERE b.user_id = #{userId}")
  @Results(value = {
      @Result(property = "id", column = "id"),
      @Result(property = "prototypeName", column = "prototypename"),
      @Result(property = "catchCopy", column = "catchcopy"),
      @Result(property = "concept", column = "concept"),
      @Result(property = "imgPath", column = "img"),
      @Result(property = "user", column = "user_id", one = @One(select = "in.tech_camp.protospace_b.repository.UserNewRepository.findById"))
  })
  List<PrototypeEntity> findBookmarkByUserId(Integer userId);

  // ブックマーク済みを判定
  @Select("SELECT COUNT(*) > 0 FROM bookmark WHERE prototype_id = #{prototypeId} AND user_id = #{userId}")
  boolean existBookmark(@Param("prototypeId") Integer prototypeId, @Param("userId") Integer userId);

  // ブックマーク削除
  @Delete("DELETE FROM bookmark WHERE prototype_id = #{prototypeId} AND user_id = #{userId}")
  void deleteBookmark(@Param("prototypeId") Integer prototypeId, @Param("userId") Integer userId);
}
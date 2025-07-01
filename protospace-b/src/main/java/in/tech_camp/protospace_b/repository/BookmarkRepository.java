package in.tech_camp.protospace_b.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.BookmarkEntity;

@Mapper
public interface BookmarkRepository {

    // ブックマークの保存
    @Insert("INSERT INTO bookmark (user_id, prototype_id) VALUES (#{user.id}, #{prototype.id})")
    void insert(BookmarkEntity bookmark);

    // ブックマーク済みを判定
    @Select("SELECT COUNT(*) > 0 FROM bookmark WHERE prototype_id = #{prototypeId} AND user_id = #{userId}")
    boolean existBookmark(@Param("prototypeId") Integer prototypeId, @Param("userId") Integer userId);

    // ブックマーク削除
    @Delete("DELETE FROM bookmark WHERE prototype_id = #{prototypeId} AND user_id = #{userId}")
    void deleteBookmark(@Param("prototypeId") Integer prototypeId, @Param("userId") Integer userId);
}
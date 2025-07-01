package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.TagEntity;

@Mapper
public interface TagRepository {
    @Insert("INSERT INTO tags(tag_name) VALUES (#{tagName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(TagEntity tag);

    @Select("SELECT * FROM tags WHERE tag_name = #{tagName}")
    TagEntity justSameTag(String tagName);

    @Delete("DELETE FROM prototype_tags WHERE prototype_id = #{id}")
    void purgeTagsFromPrototype(PrototypeEntity prototype);

    @Insert("INSERT INTO prototype_tags(prototype_id, tags_id) VALUES (#{prototype.id}, #{tag.id})")
    void setTagToPrototype(@Param("prototype") PrototypeEntity prototype, @Param("tag") TagEntity tag);

    @Select("SELECT tags.id, tags.tag_name tagName FROM tags INNER JOIN prototype_tags ON tags.id = prototype_tags.tags_id WHERE prototype_tags.prototype_id = #{prototypeId}")
    List<TagEntity> prototypeTags(Integer prototypeId);

    // BUG
    // OPTIMIZE: N+1
    @Select("SELECT p.id, p.prototypeName, p.catchCopy, p.concept, p.imgPath FROM prototype p INNER JOIN prototype_tags ON prototype.id = prototype_tags.prototype_id INNER JOIN tags ON tags.id = prototype_tags.tags_id WHERE prototype_tags.tags_id = #{tagId}")
    @Results(value = {
            @Result(property = "user", column = "prototype.user_id", one = @One(select = "in.tech_camp.protospace_b.repository.UserNewRepository.findById"))
    })
    List<PrototypeEntity> tagPrototypes(Integer tagId);
}

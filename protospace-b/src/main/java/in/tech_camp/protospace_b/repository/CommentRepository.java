package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.CommentEntity;

@Mapper
public interface CommentRepository {
  // OPTIMIZE: N+1
  @Select("SELECT c.*, u.id AS user_id, u.nickname AS user_nickname FROM comments c JOIN users u ON c.user_id = u.id WHERE c.prototype_id = #{prototypeId}")
  @Results(value = {
      @Result(property = "user.id", column = "user_id"),
      @Result(property = "user.nickname", column = "user_nickname"),
      @Result(property = "prototype", column = "prototype_id", one = @One(select = "in.tech_camp.protospace_b.repository.PrototypeShowRepository.findByPrototypeId"))
  })
  List<CommentEntity> findByPrototypeId(Integer prototypeId);

  // コメント保存
  @Insert("INSERT INTO comments (text, user_id, prototype_id) VALUES (#{text}, #{user.id}, #{prototype.id})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(CommentEntity comment);
}
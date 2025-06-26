package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.ReadStatusEntity;

@Mapper
public interface  ReadStatusRepository {
  @Insert("INSERT INTO prototype_read_status (prototype_id, user_id, read_at) VALUES (#{prototypeId}, #{userId}, NOW()) ON CONFLICT (prototype_id, user_id) DO UPDATE SET read_at = NOW()")
  void insertOrUpdate(ReadStatusEntity status);

  @Select("SELECT * FROM prototype_read_status WHERE user_id = #{userId}")
  @Results(id = "readStatusResult", value = {
    @Result(property = "prototypeId", column = "prototype_id"),
    @Result(property = "userId", column = "user_id"),
    @Result(property = "readAt", column = "read_at")
  })
  List<ReadStatusEntity> findAllByUserId(Integer userId);

  @Select("SELECT COUNT(*) > 0 FROM prototype_read_status WHERE prototype_id = #{prototypeId} AND user_id = #{userId}")
  boolean isRead(@Param("prototypeId") Integer prototypeId, @Param("userId") Integer userId);
  }

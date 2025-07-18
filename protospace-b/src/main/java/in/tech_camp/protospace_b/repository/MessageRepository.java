package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.MessageEntity;

@Mapper
public interface MessageRepository {
    @Insert("insert into messages(content, image, user_id, room_id) values(#{content}, #{image}, #{user.id}, #{room.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(MessageEntity messageEntity);

    @Select("select * from messages where room_id = #{roomId}")
    @Results(value = {
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "user", column = "user_id", one = @One(select = "in.tech_camp.protospace_b.repository.UserNewRepository.findById"))
    })
    List<MessageEntity> findByRoomId(Integer roomId);

    @Select("SELECT COUNT(*) FROM messages")
    int count();
}

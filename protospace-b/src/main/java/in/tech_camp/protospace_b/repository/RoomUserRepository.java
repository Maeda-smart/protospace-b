package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.RoomUserEntity;

@Mapper
public interface RoomUserRepository {
    @Insert("INSERT INTO room_users(user_id, room_id) VALUES(#{user.id}, #{room.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(RoomUserEntity userRoomEntity);

    @Select("select * from room_users where user_id = #{user_id}")
    @Result(property = "room", column = "room_id", one = @One(select = "in.tech_camp.protospace_b.repository.RoomRepository.findById"))
    List<RoomUserEntity> findByUserId(Integer userId);

    @Select("SELECT * FROM room_users WHERE room_id = #{roomId}")
    @Result(property = "user", column = "user_id", one = @One(select = "in.tech_camp.protospace_b.repository.UserNewRepository.findById"))
    List<RoomUserEntity> findByRoomId(Integer roomId);

}

package in.tech_camp.protospace_b.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.RoomEntity;

@Mapper
public interface RoomRepository {
    @Insert("insert into rooms(name) values(#{name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(RoomEntity roomEntity);

    @Select("select * from rooms where id = #{id}")
    RoomEntity findById(Integer id);

    @Delete("DELETE FROM rooms WHERE id = #{id}")
    void deleteById(Integer id);
}

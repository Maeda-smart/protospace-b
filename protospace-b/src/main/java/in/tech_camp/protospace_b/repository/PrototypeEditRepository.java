package in.tech_camp.protospace_b.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.protospace_b.entity.PrototypeEntity;

@Mapper
public interface PrototypeEditRepository {
    @Update("UPDATE prototype SET prototypeName = #{prototypeName}, catchCopy = #{catchCopy}, concept = #{concept}, img = #{imgPath}, published = #{published}, user_id = #{user.id} WHERE id = #{id}")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void update(PrototypeEntity prototype);
}
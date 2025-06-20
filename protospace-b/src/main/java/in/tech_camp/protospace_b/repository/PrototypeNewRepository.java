package in.tech_camp.protospace_b.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import in.tech_camp.protospace_b.entity.PrototypeEntity;

@Mapper
public interface PrototypeNewRepository {

    @Insert("INSERT INTO prototype (prototypeName, catchCopy, concept, img, user_id) " +
            "VALUES (#{prototypeName}, #{catchCopy}, #{concept}, #{img}, #{user.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(PrototypeEntity prototype);

    // 削除機能追加
    @Delete("DELETE FROM prototype WHERE id = #{id}")
    void deleteById(Integer id);
}

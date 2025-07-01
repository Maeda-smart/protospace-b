package in.tech_camp.protospace_b.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_b.entity.PinEntity;
import in.tech_camp.protospace_b.entity.PrototypeEntity;

@Mapper
public interface PinRepository {
    // entityにマッピングがうまくできていなかったため明記
    @Select("SELECT user_id AS userId, prototype_id AS prototypeId FROM pin WHERE user_id = #{userId}")
    List<PinEntity> findPinByUserId(Integer userId);

    // test
    @Select("SELECT * FROM prototype WHERE user_id = #{userId}")
    List<PrototypeEntity> findByUserId(Integer userId);

    @Insert("INSERT INTO pin (user_id,prototype_id) VALUES (#{userId}, #{prototypeId})")
    void insert(PinEntity pinEntity);

    @Select("SELECT COUNT(*) FROM pin WHERE user_id = #{userId} AND prototype_id = #{prototypeId}")
    int count(@Param("userId") Integer userId, @Param("prototypeId") Integer prototypeId);

    @Delete("DELETE FROM pin WHERE user_id = #{userId} AND prototype_id = #{prototypeId}")
    void delete(PinEntity pinEntity);
}

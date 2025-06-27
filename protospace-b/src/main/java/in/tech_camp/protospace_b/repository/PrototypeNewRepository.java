package in.tech_camp.protospace_b.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;

import in.tech_camp.protospace_b.entity.PrototypeEntity;

@Mapper
public interface PrototypeNewRepository {

    @Insert("INSERT INTO prototype (prototypeName, catchCopy, concept, img, user_id) " +
            "VALUES (#{prototypeName}, #{catchCopy}, #{concept}, #{imgPath}, #{user.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Results(value = {
        @Result(property = "id", column = "id"),
        @Result(property = "user", column = "user_id", one = @One(select = "in.tech_camp.protospace_b.repository.UserDetailRepository.findById")),
        @Result(property = "imgPath", column = "img"),
    })
    void insert(PrototypeEntity prototype);
}

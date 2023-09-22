package com.yagieottae_back_end.Repository;

import com.yagieottae_back_end.Dto.ReviewDto;
import com.yagieottae_back_end.Entity.Pill;
import com.yagieottae_back_end.Entity.Review;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>
{
    //약의 리뷰 목록 반환
    @Query("select " +
            "r.id as id," +
            "r.rate as rate," +
            "r.reviewMessage as reviewMessage," +
            "r.editDate as editDate," +
            "u.nickname as nickname," +
            "u.profileImgPath as profileImgPath " +
            "from Review r " +
            "join User u on r.user.id=u.id " +
            "where r.pill.id = :pillId " +
            "order by r.editDate desc")
    public Optional<List<ReviewDto.Response>> findByPillId(Long pillId);

    //유저가 특정 약에 작성한 리뷰들 목록
    @Query(value = "select * " +
            "from Review " +
            "where user_id=:userId and pill_id=:pillId", nativeQuery = true)
    public Optional<List<Review>> findByUserIdAndPillId(Long userId, Long pillId);
}

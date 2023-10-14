package com.yagieottae_back_end.Repository;

import com.yagieottae_back_end.Dto.AlramDto;
import com.yagieottae_back_end.Entity.Alram;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlramRepository extends JpaRepository<Alram, Long>
{
    //동일한 약으로 등록된 알람 조회
    @Query("SELECT a FROM Alram a WHERE a.user.id = :userId AND a.pill.id = :pillId")
    Optional<Alram> findExsistingAlram(Long userId, Long pillId);

    //마지막 알람 조회
    @Query("SELECT a FROM Alram a JOIN FETCH a.pill ORDER BY a.id DESC LIMIT 1")
    Optional<Alram> findLastAlram();

    //@formatter:off
    //오늘 날짜의 Alram 리스트만 조회
    @Query(value =
            "SELECT " +
            "a.id as id, " +
            "a.alramTime as alramTime, " +
            "a.days as days, " +
            "a.beforeMeal as beforeMeal, " +
            "a.dosingTime as dosingTime, " +
            "p as pill " +
            "FROM Alram a " +
            "JOIN Pill p on a.pill.id = p.id " +
            "WHERE a.user.id = :userId AND a.days LIKE %:dayOfWeek%")
    //@formatter:on
    Optional<List<AlramDto.Response>> findTodayAlrams(Long userId, String dayOfWeek);

    //전체 날짜의 Alram 리스트 조회
    //@formatter:off
    //오늘 날짜의 Alram 리스트만 조회
    @Query(value =
            "SELECT " +
            "a.id as id, " +
            "a.alramTime as alramTime, " +
            "a.days as days, " +
            "a.beforeMeal as beforeMeal, " +
            "a.dosingTime as dosingTime, " +
            "p as pill " +
            "FROM Alram a " +
            "JOIN Pill p on a.pill.id = p.id " +
            "WHERE a.user.id = :userId")
    //@formatter:on
    Optional<List<AlramDto.Response>> findAllAlrams(Long userId);
}

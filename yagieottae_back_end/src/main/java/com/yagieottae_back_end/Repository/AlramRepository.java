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

    //오늘 날짜의 Alram 리스트만 조회
    @Query(value = "SELECT a FROM Alram a JOIN FETCH a.pill WHERE a.user.id = :userId AND a.days LIKE %:today%")
    Optional<List<Alram>> findTodayAlrams(Long userId, int today);

    //전체 날짜의 Alram 리스트 조회
    @Query(value = "SELECT a FROM Alram a JOIN FETCH a.pill WHERE a.user.id = :userId")
    Optional<List<Alram>> findAllAlrams(Long userId);
}

package com.yagieottae_back_end.Repository;

import com.yagieottae_back_end.Dto.PillDto;
import com.yagieottae_back_end.Entity.Pill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PillRepository extends JpaRepository<Pill, Long>
{
    //PK 값으로 검색
    Optional<Pill> findById(Long id);

    //이름으로 검색
    @Query("SELECT p FROM Pill p WHERE p.itemName LIKE CONCAT('%',:itemName,'%')")
    Optional<Page<PillDto.Response>> findByItemName(String itemName, Pageable page);
}

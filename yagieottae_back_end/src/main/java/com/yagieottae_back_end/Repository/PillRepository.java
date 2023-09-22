package com.yagieottae_back_end.Repository;

import com.yagieottae_back_end.Entity.Pill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PillRepository extends JpaRepository<Pill, Long>
{
    //PK 값으로 검색
    Optional<Pill> findById(Long id);

    //이름으로 검색
    Optional<List<Pill>> findByItemNameContains(String itemName);
}

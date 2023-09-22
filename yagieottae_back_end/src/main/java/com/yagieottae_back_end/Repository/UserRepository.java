package com.yagieottae_back_end.Repository;

import com.yagieottae_back_end.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findFirstBy();

    Optional<User> findByUserId(String userId);

    Optional<User> findByNickname(String nickname);
}

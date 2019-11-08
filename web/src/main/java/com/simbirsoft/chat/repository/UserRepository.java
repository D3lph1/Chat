package com.simbirsoft.chat.repository;

import com.simbirsoft.chat.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.token.value = ?1 AND u.token.expiredAt > current_date")
    Optional<User> findByTokenNonExpired(String token);

    @Query("SELECT u FROM User u WHERE u.bot = true")
    Collection<User> findAllBots();

    boolean existsByEmail(String email);
}

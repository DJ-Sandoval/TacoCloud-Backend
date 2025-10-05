package com.api.Summit.API.model.repository;
import com.api.Summit.API.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.negocios WHERE u.username = :username")
    Optional<User> findByUsernameWithNegocios(@Param("username") String username);
    boolean existsByUsername(String username);
}

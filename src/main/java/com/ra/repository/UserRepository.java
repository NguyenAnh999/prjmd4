package com.ra.repository;

import com.ra.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    List<User> findUserByFullNameContains(String userName);
    Boolean existsByUsername(String userName);
    Optional<User> findUserByUsername(String userName);
    @Query("SELECT u FROM User u WHERE FUNCTION('MONTH', u.createdAt) = FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', u.createdAt) = FUNCTION('YEAR', CURRENT_DATE)")
    List<User> findUsersCreatedInCurrentMonth();

}

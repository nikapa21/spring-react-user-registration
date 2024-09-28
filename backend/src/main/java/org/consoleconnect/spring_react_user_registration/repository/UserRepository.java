package org.consoleconnect.spring_react_user_registration.repository;

import java.util.List;
import org.consoleconnect.spring_react_user_registration.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.isDeactivated = false")
    List<User> findAllActiveUsers();

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.isDeactivated = false")
    User findActiveUserById(Long id);
}

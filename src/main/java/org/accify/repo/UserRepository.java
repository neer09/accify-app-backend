package org.accify.repo;

import org.accify.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserNameAndPassword(String userName, String password);
}

package co.develhope.SpringSecurityDemo.user.repositories;

import co.develhope.SpringSecurityDemo.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // GUIDA AI QUERY METHODS
    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.definition

    boolean existsByEmail(String email);

    User findByEmail(String email);

    User findByActivationCode(String activationCode);

    User findByPasswordResetCode(String passwordResetCode);

}

package co.develhope.SpringSecurityDemo.salary.repositories;

import co.develhope.SpringSecurityDemo.salary.entities.Salary;
import co.develhope.SpringSecurityDemo.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalaryRepository extends JpaRepository<Salary, Long>  {

    Salary findByUserId(Long id);

    List<Salary> findByCreatedBy(User user);

}

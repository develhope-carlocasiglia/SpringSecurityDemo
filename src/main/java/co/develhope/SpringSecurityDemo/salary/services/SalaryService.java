package co.develhope.SpringSecurityDemo.salary.services;

import co.develhope.SpringSecurityDemo.salary.entities.Salary;
import co.develhope.SpringSecurityDemo.salary.repositories.SalaryRepository;
import co.develhope.SpringSecurityDemo.user.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SalaryService {

    @Autowired
    private SalaryRepository salaryRepository;

    public Salary save(Salary salaryInput){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (salaryInput == null)return null;
        salaryInput.setId(null);
        salaryInput.setCreatedAt(LocalDateTime.now());
        salaryInput.setCreatedBy(user);
        return salaryRepository.save(salaryInput);
    }

    public Salary update(Long id, Salary salaryInput){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (salaryInput == null)return null;
        salaryInput.setId(id);
        salaryInput.setUpdatedAt(LocalDateTime.now());
        salaryInput.setUpdatedBy(user);
        return salaryRepository.save(salaryInput);
    }

    public boolean canEdit(Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Salary> salary = salaryRepository.findById(id);
        if(!salary.isPresent())return false;
        return salary.get().getCreatedBy().getId() == user.getId();
    }
}

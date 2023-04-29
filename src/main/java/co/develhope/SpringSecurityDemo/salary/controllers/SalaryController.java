package co.develhope.SpringSecurityDemo.salary.controllers;

import co.develhope.SpringSecurityDemo.salary.entities.Salary;
import co.develhope.SpringSecurityDemo.salary.repositories.SalaryRepository;
import co.develhope.SpringSecurityDemo.salary.services.SalaryService;
import co.develhope.SpringSecurityDemo.user.entities.User;
import co.develhope.SpringSecurityDemo.user.utils.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/app/salary")
@PreAuthorize("hasRole('"+ Roles.REGISTERED + "') OR hasRole('"+Roles.ADMIN+"')")
public class SalaryController {

    @Autowired
    SalaryRepository salaryRepository;

    @Autowired
    SalaryService salaryService;

    //create salary
    @PostMapping
    public ResponseEntity<Salary> create(@RequestBody Salary salary){
        return ResponseEntity.ok(salaryService.save(salary));
    }

    //get salary by user id
    @GetMapping("/user/{id}")
    public Salary getSalaryByUid(@PathVariable Long id){
        return salaryRepository.findByUserId(id);
    }

    //get salary by salary id
    @GetMapping("/{id}")
    //@PostAuthorize("hasRole('"+Roles.ADMIN + "') OR returnObject.body == null OR returnObject.body.createdBy.id == authentication.principal.id")
    public ResponseEntity<Salary> getSingle(@PathVariable Long id){
        Optional<Salary> salary = salaryRepository.findById(id);
        if (!salary.isPresent())return ResponseEntity.notFound().build();
        return ResponseEntity.ok(salary.get());
    }

    //get all salary
    @GetMapping
    public ResponseEntity<List<Salary>> getAll(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = user.getRoles().stream().filter(role -> role.getName().equals(Roles.ADMIN)).findAny().isPresent();
        if (isAdmin){
            return ResponseEntity.ok(salaryRepository.findAll());
        }else {
            return ResponseEntity.ok(salaryRepository.findByCreatedBy(user));
        }
    }

    //edit salary by id
    @PutMapping("/{id}")
    public ResponseEntity<Salary> update(@RequestBody Salary salary, @PathVariable Long id){
        if(!salaryService.canEdit(id)){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(salaryService.update(id, salary));
    }

    //delete salary by id
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        if(!salaryService.canEdit(id)){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        salaryRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}


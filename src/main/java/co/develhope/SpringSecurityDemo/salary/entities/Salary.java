package co.develhope.SpringSecurityDemo.salary.entities;

import co.develhope.SpringSecurityDemo.user.entities.User;
import co.develhope.SpringSecurityDemo.user.utils.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "salary")
public class Salary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private double amount;

    @OneToOne
    private User user;

    public Salary() {
    }

    public Salary(Long id, double amount, User user) {
        this.id = id;
        this.amount = amount;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

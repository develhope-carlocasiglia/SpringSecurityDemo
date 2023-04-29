package co.develhope.SpringSecurityDemo.auth.entities;

public class SignupDTO {

    /** User name */
    private String name;
    /** User surname */
    private String surname;
    /** User email */
    private String email;
    /** User password clear */
    private String password;

    public SignupDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

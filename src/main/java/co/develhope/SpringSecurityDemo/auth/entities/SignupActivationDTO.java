package co.develhope.SpringSecurityDemo.auth.entities;

public class SignupActivationDTO {

    private String activationCode;

    public SignupActivationDTO() {
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }
}

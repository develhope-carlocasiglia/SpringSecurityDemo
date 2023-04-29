package co.develhope.SpringSecurityDemo.auth.entities;

import co.develhope.SpringSecurityDemo.user.entities.User;

public class LoginRTO {

    private User user;
    private String JWT; // Json Web Token

    public LoginRTO() {
    }

    public LoginRTO(User user, String JWT) {
        this.user = user;
        this.JWT = JWT;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getJWT() {
        return JWT;
    }

    public void setJWT(String JWT) {
        this.JWT = JWT;
    }
}

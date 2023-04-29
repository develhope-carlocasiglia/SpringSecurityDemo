package co.develhope.SpringSecurityDemo.auth.controller;

import co.develhope.SpringSecurityDemo.auth.entities.SignupActivationDTO;
import co.develhope.SpringSecurityDemo.auth.entities.SignupDTO;
import co.develhope.SpringSecurityDemo.auth.services.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class SignupController {

    @Autowired
    private SignupService signupService;

    /*
    POST localhost:8080/auth/signup
    {
        "name": "Carlo",
        "surname": "Casiglia",
        "email": "carlo.casiglia@gmail.com",
        "password": "prova"
     }
     */
    @PostMapping("/signup")
    public void signup(@RequestBody SignupDTO signupDTO) throws Exception {
        signupService.signup(signupDTO);
    }

    @PostMapping("/signup/activation")
    public void signup(@RequestBody SignupActivationDTO signupActivationDTO) throws Exception {
        signupService.activate(signupActivationDTO);
    }
}

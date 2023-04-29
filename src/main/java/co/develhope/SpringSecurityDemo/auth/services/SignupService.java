package co.develhope.SpringSecurityDemo.auth.services;

import co.develhope.SpringSecurityDemo.user.utils.Roles;
import co.develhope.SpringSecurityDemo.user.entities.User;
import co.develhope.SpringSecurityDemo.auth.entities.SignupActivationDTO;
import co.develhope.SpringSecurityDemo.auth.entities.SignupDTO;
import co.develhope.SpringSecurityDemo.email.MailNotificationService;
import co.develhope.SpringSecurityDemo.user.entities.Role;
import co.develhope.SpringSecurityDemo.user.repositories.RoleRepository;
import co.develhope.SpringSecurityDemo.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class SignupService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MailNotificationService mailNotificationService;

    @Autowired // prendi questo bean
    private PasswordEncoder passwordEncoder;

    public User signup(SignupDTO signupDTO) throws Exception { //SignupDTO rappresenta in Java l'oggetto body su Postman

        if (userRepository.existsByEmail(signupDTO.getEmail())) throw new Exception("Email " + signupDTO.getEmail() + " already exist");
        //User userInDB = userRepository.findByEmail(signupDTO.getEmail());
        //if(userInDB != null) throw new Exception("User already exist");

        // creo nuovo user e setto i parametri necessari
        User user = new User();
        user.setName(signupDTO.getName());
        user.setEmail(signupDTO.getEmail());
        user.setSurname(signupDTO.getSurname());
        user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));// la password viene codificata con PasswordEncoder
        //user.setActive(false); // aggiunto parametro in User class

        //genera un codice univoco di 36 caratteri
        // UUID = Universal Unique IDentifier
        user.setActivationCode(UUID.randomUUID().toString());

        Set<Role> roles = new HashSet<>();

        Optional<Role> userRole =roleRepository.findByName(Roles.REGISTERED);
        if(!userRole.isPresent()) throw new Exception("Cannot set user role");
        roles.add(userRole.get());
        user.setRoles(roles);

        mailNotificationService.sendActivationEmail(user);// invio mail di attivazione
        return userRepository.save(user); // ritorniamo l'user salvato
    }

    public User activate(SignupActivationDTO signupActivationDTO) throws Exception {
        User user = userRepository.findByActivationCode(signupActivationDTO.getActivationCode());
        if(user == null) throw new Exception("User Not found");
        user.setActive(true);
        user.setActivationCode(null);
        return userRepository.save(user);
    }
}

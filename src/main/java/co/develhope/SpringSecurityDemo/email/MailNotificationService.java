package co.develhope.SpringSecurityDemo.email;

import co.develhope.SpringSecurityDemo.user.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailNotificationService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendActivationEmail(User user) {
        SimpleMailMessage sms = new SimpleMailMessage();
        sms.setTo(user.getEmail());
        sms.setFrom(fromEmail);
        sms.setReplyTo(fromEmail);
        sms.setSubject("Ti sei iscritto alla piattaforma");
        sms.setText("Il codice di attivazione è:" + user.getActivationCode());
        emailSender.send(sms);
    }

    public void sendPasswordResetMail(User user) {
        SimpleMailMessage sms = new SimpleMailMessage();
        sms.setTo(user.getEmail());
        sms.setFrom("development@develhope.co");
        sms.setReplyTo("development@develhope.co");
        sms.setSubject("Ti sei iscritto alla piattaforma");
        sms.setText("Il codice di attivazione è:" + user.getPasswordResetCode());
        emailSender.send(sms);
    }
}

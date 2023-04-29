package co.develhope.SpringSecurityDemo;

import co.develhope.SpringSecurityDemo.auth.services.LoginService;
import co.develhope.SpringSecurityDemo.user.entities.User;
import co.develhope.SpringSecurityDemo.user.repositories.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepo;

    private Collection<? extends GrantedAuthority> getAuthorities(User user){
        if (user == null || !user.isActive()) return List.of();
        return Arrays.asList(user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role.getName())).toArray(SimpleGrantedAuthority[]::new));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        // get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            chain.doFilter(request,response);
            return;
        }

        // get JWT token and validate
        final String token; //from header Bearer
        try {
            /*
            L'header Authorization creato da Postman è fatto così:
            Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IlJFR0lTVEVSRUQiLCJpc3MiOiJkZXZlbGhvcGUtZGVtbyIsImlkIjo2LCJleHAiOjE2ODM5MDMyMjgsImlhdCI6MTY4MjYwNzIyOH0.YUY3IiS4NTs70Wexup-2AgBzJf3mqU5Kmsl5R2Qpyp2JqYYC5VtaecExDNbAKknzHgKQu2OCvzWT2r7KRI-Avg
            La parola Bearer a noi non interessa, ci interessa solo il token, quindi facciamo split(" ")
            dividendo l'header in due pezzi ("Bearer" e "eyJ0eXAiO....") e poi prendiamo il secondo pezzo
            trim() toglie tutti gli spazi
             */
            token = header.split(" ")[1].trim();
        }catch (JWTVerificationException ex){
            chain.doFilter(request,response);
            return;
        }

        DecodedJWT decoded;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(LoginService.JWT_SECRET)).withIssuer("develhope-demo").build();
            decoded = verifier.verify(token);
        }catch (JWTVerificationException ex){
            chain.doFilter(request,response);
            return;
        }

        // get user identity and set it on the spring security context
        Optional<User> userDetails = userRepo.findById(decoded.getClaim("id").asLong());
        if(!userDetails.isPresent() || !userDetails.get().isActive()){
            chain.doFilter(request,response);
            return;
        }

        User user = userDetails.get();
        //user.setPassword(null);
        user.setActivationCode(null);
        user.setPasswordResetCode(null);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user,null, getAuthorities(user)
        );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request,response);
    }
}
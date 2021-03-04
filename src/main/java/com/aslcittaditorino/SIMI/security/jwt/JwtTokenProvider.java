package com.aslcittaditorino.SIMI.security.jwt;

import com.aslcittaditorino.phonemanager.repositories.UserRepository;
import com.aslcittaditorino.phonemanager.security.User;
import com.aslcittaditorino.phonemanager.services.StatisticService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class JwtTokenProvider {
    @Autowired
    UserRepository userRepository;
    @Autowired
    StatisticService service;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${application.name}")
    private String appName;
    private String secretKey;

    public boolean checkSingleLogin(String token) {
        return service.checkSingleLogin(token);
    }


    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public List<String> getUserRoleForApp(String username){
       Optional<User> temp = userRepository.findById(username);
       if(temp.isEmpty()){
           throw new ResponseStatusException(HttpStatus.NOT_FOUND);
       }else{
           return temp.get().getRoles();
       }

    }

    public String generateToken(User user){
        if(checkCredentials(user))
        {
            try {
                Algorithm algorithm = Algorithm.HMAC256("secret");
                String token = JWT.create()
                        .withIssuer("auth0")
                        .withClaim("username",user.getUsername() )
                        .withClaim("password",user.getPassword() )
                        .withClaim("application",user.getApplication() )
                        .withClaim("roles",user.getRoles() )
                        .sign(algorithm);
                return token;
            } catch (JWTCreationException exception){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public boolean checkCredentials(User user){
        Optional<User> temp =userRepository.findById(user.getUsername());
        if(!temp.isEmpty()){
            return temp.get().getPassword().equals(user.getPassword());
        }
        return false;

    }

    public Authentication getAuthentication(String token) {
        Algorithm algorithm = Algorithm.HMAC256(Base64.getEncoder().encodeToString(secret.getBytes()));
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("auth0")
                .build();
        DecodedJWT jwt = verifier.verify(token);

        User user = new User();
        user.setUsername(jwt.getClaim("username").asString());
        user.setApplication(jwt.getClaim("application").asString());
        user.setRoles(jwt.getClaim("roles").asList(String.class));

        return new UsernamePasswordAuthenticationToken(
                user,
                "",
                user.getAuthorities()
        );
    }



    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {

        try {
            System.out.println(token);
            Algorithm algorithm = Algorithm.HMAC256(Base64.getEncoder().encodeToString(secret.getBytes()));
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            if (!jwt.getClaim("application").asString().equals(appName))
                throw new JWTVerificationException("Expired or invalid JWT token!");
            return !jwt.getExpiresAt().before(new Date());
        } catch (JWTVerificationException exception) {
            throw new JwtException("Expired or invalid JWT token!");
        }
    }
}

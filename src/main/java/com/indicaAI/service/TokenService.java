package com.indicaAI.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.indicaAI.exception.JWTCreationException;
import com.indicaAI.model.User;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;



@Service
public class TokenService {
    String code = "teste";

    public String generateToken(User user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(code);
            return JWT.create()
                    .withIssuer("api-indicaAi")
                    .withSubject(user.getUsername())
                    .withExpiresAt(tokenExperirationDate())
                    .sign(algorithm);
        }catch (Exception e) {
            throw new JWTCreationException("Erro ao gerar token JWT");
        }
    }

    private Instant tokenExperirationDate() {
        return LocalDateTime.now().plusHours(24).toInstant(ZoneOffset.of("-03:00"));
    }

    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(code);
        return JWT.require(algorithm)
                .withIssuer("api-indicaAi")
                .build()
                .verify(token)
                .getSubject();
    }
}

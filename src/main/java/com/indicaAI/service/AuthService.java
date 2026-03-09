package com.indicaAI.service;

import com.indicaAI.dtos.CadastroDto;
import com.indicaAI.dtos.CadastroResponse;
import com.indicaAI.dtos.LoginDto;
import com.indicaAI.dtos.LoginRespose;
import com.indicaAI.exception.AlreadyExistException;
import com.indicaAI.exception.NotExistEception;
import com.indicaAI.model.User;
import com.indicaAI.model.roles.UserRole;
import com.indicaAI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    TokenService tokenService;
    public LoginRespose login (LoginDto loginDto){
        User user = userRepository.findByEmail(loginDto.email())
                .orElseThrow(() -> new NotExistEception("Email não cadastrado."));

        var authToken = new UsernamePasswordAuthenticationToken(
                loginDto.email(),
                loginDto.password()
        );
        authenticationManager.authenticate(authToken);
        String token = tokenService.generateToken(user);
        return new LoginRespose(token);
    }

    public CadastroResponse cadastro(CadastroDto cadastroDto) {
        // Verifica se email já existe
        if (userRepository.findByEmail(cadastroDto.email()).isPresent()) {
            throw new AlreadyExistException("Email já cadastrado.");
        }
        User user = new User();
        user.setUsername(cadastroDto.username());
        user.setEmail(cadastroDto.email());
        user.setSenha(encoder.encode(cadastroDto.password()));
        user.setRole(UserRole.USER);
        user.setCreated_at(LocalDateTime.now());
        userRepository.save(user);
        return new CadastroResponse("Usuário cadastrado com sucesso.");
    }








}

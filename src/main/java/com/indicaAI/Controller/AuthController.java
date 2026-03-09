package com.indicaAI.Controller;

import com.indicaAI.dtos.CadastroDto;
import com.indicaAI.dtos.CadastroResponse;
import com.indicaAI.dtos.LoginDto;
import com.indicaAI.dtos.LoginRespose;
import com.indicaAI.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginRespose> login(@RequestBody @Valid LoginDto loginDto) {
        LoginRespose response = authService.login(loginDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<CadastroResponse> cadastro(@RequestBody @Valid CadastroDto cadastroDto) {
        CadastroResponse response = authService.cadastro(cadastroDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
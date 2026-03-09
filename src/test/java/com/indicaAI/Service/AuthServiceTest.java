package com.indicaAI.Service;

import com.indicaAI.dtos.CadastroDto;
import com.indicaAI.dtos.CadastroResponse;
import com.indicaAI.dtos.LoginDto;
import com.indicaAI.dtos.LoginRespose;
import com.indicaAI.exception.AlreadyExistException;
import com.indicaAI.exception.NotExistEception;
import com.indicaAI.model.User;
import com.indicaAI.repository.UserRepository;
import com.indicaAI.service.AuthService;
import com.indicaAI.service.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {



    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private TokenService tokenService;


    @Test
    @DisplayName("Login com sucesso")
    void loginSucesso() {
        LoginDto dto = new LoginDto("test@email.com", "123456");
        User user = new User();
        user.setEmail("test@email.com");

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(user));
        when(tokenService.generateToken(user)).thenReturn("token-fake");

        LoginRespose response = authService.login(dto);

        assertNotNull(response);
        assertEquals("token-fake", response.token());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("Login com email não cadastrado")
    void loginEmailNaoExiste() {
        LoginDto dto = new LoginDto("naoexiste@email.com", "123456");

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());

        assertThrows(NotExistEception.class, () -> authService.login(dto));
        verify(authenticationManager, never()).authenticate(any());
    }

    // ==================== CADASTRO ====================

    @Test
    @DisplayName("Cadastro com sucesso")
    void cadastroSucesso() {
        CadastroDto dto = new CadastroDto("joao", "joao@email.com", "123456");

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(encoder.encode(dto.password())).thenReturn("senha-encodada");

        CadastroResponse response = authService.cadastro(dto);

        assertNotNull(response);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Cadastro com email já existente")
    void cadastroEmailJaExiste() {
        CadastroDto dto = new CadastroDto("joao", "joao@email.com", "123456");

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(new User()));

        assertThrows(AlreadyExistException.class, () -> authService.cadastro(dto));
        verify(userRepository, never()).save(any());
    }

}

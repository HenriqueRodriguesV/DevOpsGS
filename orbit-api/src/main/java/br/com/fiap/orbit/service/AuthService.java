package br.com.fiap.orbit.service;

import br.com.fiap.orbit.dto.request.AuthRequest;
import br.com.fiap.orbit.dto.request.RegisterRequest;
import br.com.fiap.orbit.dto.response.AuthResponse;
import br.com.fiap.orbit.exception.BusinessException;
import br.com.fiap.orbit.model.Usuario;
import br.com.fiap.orbit.repository.UsuarioRepository;
import br.com.fiap.orbit.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse registrar(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException("E-mail já cadastrado: " + request.email());
        }
        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(passwordEncoder.encode(request.senha()))
                .role("ROLE_USER")
                .build();
        usuarioRepository.save(usuario);
        String token = jwtService.generateToken(usuario);
        return new AuthResponse(token, "Bearer", usuario.getEmail(), usuario.getNome());
    }

    public AuthResponse autenticar(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
        String token = jwtService.generateToken(usuario);
        return new AuthResponse(token, "Bearer", usuario.getEmail(), usuario.getNome());
    }
}

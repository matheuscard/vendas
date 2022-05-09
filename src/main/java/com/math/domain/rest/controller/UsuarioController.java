package com.math.domain.rest.controller;

import com.math.domain.entity.Pedido;
import com.math.domain.entity.Usuario;
import com.math.domain.rest.dto.CredenciaisDTO;
import com.math.domain.rest.dto.InformacoesPedidoDTO;
import com.math.domain.rest.dto.TokenDTO;
import com.math.domain.services.impl.UsuarioServiceImpl;
import com.math.exception.SenhaInvalidaException;
import com.math.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioServiceImpl usuarioService;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar(@RequestBody @Valid Usuario usuario){
        String senhaCriptografada =  passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return usuarioService.salvar(usuario);
    }
    @PostMapping("/auth")
    public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais){
        try{
            Usuario usuario = Usuario.builder()
                    .login(credenciais.getLogin())
                    .senha(credenciais.getSenha()).build();
            UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);
            String token = jwtService.gerarToken(usuario);
            return new TokenDTO(usuario.getLogin(),token);
        }catch (UsernameNotFoundException | SenhaInvalidaException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}

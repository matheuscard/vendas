package com.math.security.jwt;

import com.math.domain.services.impl.UsuarioServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private UsuarioServiceImpl usuarioService;

    public JwtAuthFilter(JwtService jwtService, UsuarioServiceImpl usuarioService) {
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain) throws ServletException, IOException {
            String authorization =  httpServletRequest.getHeader("Authorization");
        /**
         *Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmdWxhbm8iLCJleHAiOjE2MzQ5MjQ0Mjh9.I6iIX1oldoBomzkKTARMdlImZ4Q4yxvyz_HCAhb2kngHQjS-m3OD8cIMU9v2YjejV1VG-q7BRYS5MXFK1V2r0w
         */
        if(authorization !=null && authorization.startsWith("Bearer")){
                String token = authorization.split(" ")[1];
                if(jwtService.isTokenValido(token)){
                    String loginUsuario = jwtService.obterLoginUsuario(token);
                    UserDetails usuario = usuarioService.loadUserByUsername(loginUsuario);
                    UsernamePasswordAuthenticationToken user = new
                            UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

                    user.setDetails(new WebAuthenticationDetailsSource()
                            .buildDetails(httpServletRequest));
                    //Dessa forma, jogamos esse usuário que carregamos através do
                    // UserDetails para dentro do contexto do SpringSecurity.
                    SecurityContextHolder.getContext().setAuthentication(user);
                }
            }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}

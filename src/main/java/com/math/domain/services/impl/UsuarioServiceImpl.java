package com.math.domain.services.impl;

import com.math.domain.entity.Usuario;
import com.math.domain.repository.UsuarioRepository;
import com.math.exception.SenhaInvalidaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    public UserDetails autenticar(Usuario u){
       UserDetails usuario =  loadUserByUsername(u.getLogin());
       boolean senhasBatem = encoder.matches(u.getSenha(), usuario.getPassword());

       if(senhasBatem){
           return usuario;
       }
       throw new SenhaInvalidaException();
    }
    /**
     * @param username
     * @target Carregar o usuário da base de dados a partir do seu login.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u= repository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado na base de dados."));
        String[] roles = u.isAdmin() ? new String[]{"ADMIN", "USER"} : new String[]{"USER"};
        return User.builder()
                .username(u.getLogin())
                .password(u.getSenha())
                .roles(roles)
                .build();

    }
    @Transactional
    public Usuario salvar(Usuario usuario){
        return repository.save(usuario);
    }
}

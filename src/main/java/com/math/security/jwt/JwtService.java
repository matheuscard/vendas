package com.math.security.jwt;

import com.math.VendasApplication;
import com.math.domain.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Service
public class JwtService {
    @Value("${security.jwt.expiracao}")
    private String expiracao;
    @Value("${security.jwt.chave-assinatura}")
    private String chaveAssinatura;

    public String gerarToken(Usuario usuario){
        long expString = Long.valueOf(expiracao);
        LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(expString);
        Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);
//Da para customizar os claimns e a partir disso é só setar no
        // método setClains.. Jwts.setClaims
//        HashMap<String, Object> claims = new HashMap<>();
//        claims.put("email-user","usuario@gmail.com");
//        claims.put("roles","admin");

        return Jwts
                .builder()
                .setSubject(usuario.getLogin())
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512,chaveAssinatura)
                .compact();
    }

    public boolean isTokenValido(String token){
        try{
            Claims claims = obterClaims(token);
            Date dataExpiracao = claims.getExpiration();
            LocalDateTime data = dataExpiracao.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return !LocalDateTime.now().isAfter(data);
        }catch (Exception e){
            return false;
        }
    }

    public String obterLoginUsuario(String token) throws ExpiredJwtException{
        return obterClaims(token).getSubject();
    }
    private Claims obterClaims(String token) throws ExpiredJwtException {
        return Jwts
                .parser()
                .setSigningKey(chaveAssinatura)
                .parseClaimsJws(token)
                .getBody();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext contexto = SpringApplication.run(VendasApplication.class);
        JwtService service = contexto.getBean(JwtService.class);
        Usuario usuario = Usuario.builder().login("fulano").build();
        System.out.println("================Deixando as coisas mais seguras ============================");
       String token = service.gerarToken(usuario);
        System.out.println(token);

        boolean isTokenValid= service.isTokenValido(token);

        System.out.println("O token é válido? " + isTokenValid);

        System.out.println(service.obterLoginUsuario(token));

    }
}

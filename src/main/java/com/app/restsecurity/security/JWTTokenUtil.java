package com.app.restsecurity.security;

import com.app.restsecurity.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public class JWTTokenUtil {

    @Autowired
    private UserService userService;

    @Value("${jwt.validity.access-token}")
    private Long ACCESS_TOKEN_VALIDITY_DURATION;
    @Value("${jwt.validity.refresh-token}")
    private Long REFRESH_TOKEN_VALIDITY_DURATION;
    @Value("${jwt.tag.issuer}")
    private String TOKEN_ISSUER;
    @Value("${jwt.tag.issued-date}")
    private String ISSUED_DATE_TAG;
    @Value("${jwt.tag.roles}")
    private String ROLES_TAG;
    @Value("${jwt.secret}")
    private String SECRET;

    public String generateAccessToken(String phone, List<SimpleGrantedAuthority> authorities){
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes(StandardCharsets.UTF_8));
        return JWT.create()
                .withSubject(phone)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_DURATION))
                .withIssuer(TOKEN_ISSUER)
                .withClaim(ISSUED_DATE_TAG, new Date())
                .withClaim(ROLES_TAG, authorities.stream().map(Object::toString).toList())
                .sign(algorithm);
    }

    public String generateRefreshToken(String phone, List<SimpleGrantedAuthority> authorities){
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes(StandardCharsets.UTF_8));
        return JWT.create()
                .withSubject(phone)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY_DURATION))
                .withIssuer(TOKEN_ISSUER)
                .withClaim(ISSUED_DATE_TAG, new Date())
                .withClaim(ROLES_TAG, authorities.stream().map(Object::toString).toList())
                .sign(algorithm);
    }

    public String retrieveTokenFromRequest(HttpServletRequest request){
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            return authorizationHeader.substring(7);
        }
        return null;
    }

    public DecodedJWT getDecodedToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes(StandardCharsets.UTF_8));
        return JWT.require(algorithm).build().verify(token);
    }

    public String retrievePhoneFromToken(String token) throws JWTVerificationException {
        return getDecodedToken(token).getSubject();
    }

    public List<String> retrieveRolesFromToken(String token) throws JWTVerificationException{
        return getDecodedToken(token).getClaim(ROLES_TAG).asList(String.class);
    }

    public void authorizeToken(String token, String requestURI) throws JWTVerificationException, AuthenticationException {
        String phone = retrievePhoneFromToken(token);
        if (!userService.existsByPhone(phone))
            throw new UsernameNotFoundException("The phone doesn't exist!");
        else {
            var roles = retrieveRolesFromToken(token);
            if (
                    (requestURI.startsWith("/api/private") && !roles.contains("USER")) ||
                            (requestURI.startsWith("/api/admin") && !roles.contains("ADMIN"))
            ) throw new BadCredentialsException("The user doesn't have enough permissions");
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(phone, "", authorities));
        }
    }

}
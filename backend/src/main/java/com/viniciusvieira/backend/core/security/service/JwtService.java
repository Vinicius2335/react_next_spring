//package com.viniciusvieira.backend.core.security.service;
//
//import com.viniciusvieira.backend.domain.exception.TokenException;
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import io.jsonwebtoken.security.SignatureException;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import java.security.Key;
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//@Service
//@RequiredArgsConstructor
//public class JwtService {
//    @Value("${jwt.secret}")
//    private String secretKey;
//
//    // Extraindo username do token
//    public String extractUsername(String token, HttpServletRequest request) {
//        return extractClaim(request, token, Claims::getSubject);
//    }
//
//    public <T> T extractClaim(HttpServletRequest request, String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaimsAndValidating(request, token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims extractAllClaimsAndValidating(HttpServletRequest request, String token) {
//        String attribute = "validacaoToken";
//        try {
//            return Jwts.parserBuilder()
//                    .setSigningKey(getSigningKey())
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//
//        } catch (SignatureException e) {
//            request.setAttribute(attribute, "Assinatura do token Inválida...");
//            throw new TokenException("Assinatura Inválida..." + e.getMessage());
//        } catch (MalformedJwtException e){
//            request.setAttribute(attribute, "JWT token inválido...");
//            throw new TokenException("JWT token inválido..." + e.getMessage());
//        } catch (ExpiredJwtException e) {
//            request.setAttribute(attribute, "Token expirado...");
//            throw new TokenException("Token expirado..." + e.getMessage());
//        } catch (UnsupportedJwtException e) {
//            request.setAttribute(attribute, "Token não suportado...");
//            throw new TokenException("Token não suportado..." + e.getMessage());
//        }
//    }
//
//    private Key getSigningKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    // Cria um token simples
//    public String generateSimpleToken(UserDetails userDetails) {
//        return generateToken(new HashMap<>(), userDetails);
//    }
//
//    // Permite personalizar o token atraves do map
//    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
//        return Jwts.builder()
//                .setClaims(extraClaims)
//                .setSubject(userDetails.getUsername())
//                .setIssuedAt(Date.from(Instant.now()))
//                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    // Validando Token
//    public boolean isTokenValid(HttpServletRequest request, String token, UserDetails userDetails) {
//        final String extractUsername = extractUsername(token, request);
//        Date extractExpiration = extractClaim(request, token, Claims::getExpiration);
//
//        boolean isUsernameTokenValid = extractUsername.equals(userDetails.getUsername());
//        boolean isTokenExpired = extractExpiration.before(new Date());
//
//        return isUsernameTokenValid && !isTokenExpired;
//    }
//}

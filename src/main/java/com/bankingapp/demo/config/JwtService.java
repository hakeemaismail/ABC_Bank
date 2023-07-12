package com.bankingapp.demo.config;

import com.bankingapp.demo.entities.User;
import com.bankingapp.demo.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Autowired
    UserRepository userRepository;
    private static final String SECRET_KEY = "ikqPRw+Vt4J5cp8c+XJtZFYpKnfzzoKt63nRe7PL7ylEmVHi4E7QAJvIcIQzQakt68c+HVWHPAbZBKTHid0d4P0ZqYSmvGq4sUSulDmm2IpvmKqSwJYnD572RPEOeMWjPYAAeVt/d2nZYDRwkUwFD6vk/63R1VPMnCgaKzqw80BFWrXaJtl0gXP63Tek6jl+ge7DjJASgX4YaLvL/iN7wAJkS3uJR0XuIRd6Yk1ZgsCClC1fvook5BRfIckm8DLcMmji02mVMkmMDjgB885bfQKm/j7J9uojZZircoYuq1RzFwawT8sfzNME38SSYhuceheiRLRPYgacLHG3AkQKt5cix1sDvw8ua/KfwyGF3Jw=";
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts
                .builder().setClaims(extraClaims).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 1000*60*24)).signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return(username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    public User getAuthUser(HttpServletRequest req) {
        System.out.println(req.getUserPrincipal().getName());
        User authUser = userRepository.findByEmail(req.getUserPrincipal().getName()).get();

        System.out.println("Util function");
//        System.out.println(authUser.toString());
        return authUser;
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

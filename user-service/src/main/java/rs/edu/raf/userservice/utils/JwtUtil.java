package rs.edu.raf.userservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import rs.edu.raf.userservice.domains.dto.AuthenticationDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "MY JWT SECRET KEY";

    public Claims extractAllClaims(String token) {

        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {

        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public String generateToken(AuthenticationDetails authenticationDetails) {
        Map<String, Object> claims = new HashMap<>();

        List<String> permissions = authenticationDetails.getPermissions().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        claims.put("id", authenticationDetails.getId());
        claims.put("role", authenticationDetails.getRole().getRoleName());
        claims.put("permissions", permissions);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(authenticationDetails.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }


    public boolean validateToken(String token, UserDetails user) {
        return (user.getUsername().equals(extractUsername(token)) && !isTokenExpired(token));
    }
}
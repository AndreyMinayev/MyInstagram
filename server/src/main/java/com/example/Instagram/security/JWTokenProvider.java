package com.example.Instagram.security;

import com.example.Instagram.entity.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTokenProvider {

    public static final Logger LOG = LoggerFactory.getLogger(JWTokenProvider.class);

    public String generateToken(Authentication authentication) {

        // getting user
        User user = (User) authentication.getPrincipal();
        // getting userId
        String userId = Long.toString(user.getId());

        // now time
        Date now = new Date(System.currentTimeMillis());
        // expiry time
        Date expiryDate = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);

        // create claimsMap
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", userId);
        claimsMap.put("username", user.getEmail());
        claimsMap.put("name", user.getName());
        claimsMap.put("surname", user.getSurname());

        return Jwts.builder()
                .setSubject(userId)
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .compact();
    }


    // validating token , if error than  log and return false
    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET)
                    .parseClaimsJws(token);
            return true;
        }catch (SignatureException|
                MalformedJwtException|
                ExpiredJwtException|
                UnsupportedJwtException|
                IllegalArgumentException exception){
            LOG.error(exception.getMessage());
            return false;
        }
    }


    // method that helps to get ID from claims map
    public Long getUserIdFromToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET)
                .parseClaimsJws(token)
                .getBody();

        String id = (String)claims.get("id");
        return Long.parseLong(id);

    }
}

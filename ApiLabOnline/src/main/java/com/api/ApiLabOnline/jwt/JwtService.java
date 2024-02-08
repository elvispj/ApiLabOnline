package com.api.ApiLabOnline.jwt;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	private static final String SECRET_KEY = "5DFG5DF5DFG5DFG5DFG5DFG5DFG5DFG5DFG5DFG5DFG5DFG5DFGG54DF35G43FD54G35FD4G35DF4DG35DF4";

	public String getToken(UserDetails user) {
		return getToken(new HashMap<>(), user);
	}

	private String getToken(Map<String, Object> extraClaims, UserDetails user) {
		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(user.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+(1000*60*60*24)))
				.signWith(getKey(), SignatureAlgorithm.HS256)
				.setHeaderParam("Access-Control-Expose-Headers", "*")
				.compact();
	}

	private Key getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

    public String getUserNameFromToken(String token) {
        return getClaims(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        
        String username=getUserNameFromToken(token);
        
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    private Claims getAllClaims(String token) {
    	 return Jwts
    	            .parser()
    	            .setSigningKey(getKey())
    	            .build()
    	            .parseClaimsJws(token)
    	            .getBody();
    }
    
    private <T> T getClaims(String token, Function<Claims, T> claimResolver){
        Claims claims = getAllClaims(token);
        
        return claimResolver.apply(claims);
    }
    
    private Date getExpiration(String token) {
        return getClaims(token, Claims::getExpiration);
    }
    
    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }


}

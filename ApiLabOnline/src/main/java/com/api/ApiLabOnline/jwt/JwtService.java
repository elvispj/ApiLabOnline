package com.api.ApiLabOnline.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.entity.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	private static final String SECRET_KEY = "5DFG5DF5DFG5DFG5DFG5DFG5DFG5DFG5DFG5DFG5DFG5DFG5DFGG54DF35G43FD54G35FD4G35DF4DG35DF4";

	public String getToken(Usuario user) {
		return getToken(new HashMap<>(), user);
	}
	
	public String getRefreshToken(Usuario user) {
		return getRefreshToken(new HashMap<>(), user);
	}

	private String getToken(Map<String, Object> extraClaims, Usuario user) {
		return Jwts
				.builder()
				.claims(extraClaims)
				.subject(user.getUsuariocorreo())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis()+(1000*60*1)))
//				.signWith(getKey(), Jwts.SIG.HS256)
//				.signWith(getKey(), SignatureAlgorithm.HS256)
				.signWith(getKey())
				.setHeaderParam("Access-Control-Expose-Headers", "*")
				.compact();
	}

	private String getRefreshToken(Map<String, Object> extraClaims, Usuario user) {
		return Jwts
				.builder()
				.claims(extraClaims)
				.subject(user.getUsuariocorreo())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis()+(1000*60*40)))
//				.signWith(getKey(), Jwts.SIG.HS256)
//				.signWith(getKey(), SignatureAlgorithm.HS256)
				.signWith(getKey())
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

    public boolean isTokenValid(String token, String usuariocorreo) {
        
        String username=getUserNameFromToken(token);
        
        return (username.equals(usuariocorreo) && !isTokenExpired(token));
    }

    public boolean isTokenRefreshValid(String token, String usuariocorreo) {
        
        String username=getUserNameFromToken(token);
        
        return (username.equals(usuariocorreo));
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

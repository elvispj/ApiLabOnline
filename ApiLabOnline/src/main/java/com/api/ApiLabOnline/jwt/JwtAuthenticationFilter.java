package com.api.ApiLabOnline.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.api.ApiLabOnline.services.UsuarioServices;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private Logger log = LogManager.getLogger(this.getClass());

	@Autowired
    private JwtService jwtService;

//	@Autowired
//    private UserDetailsService userDetailsService;
	@Autowired
	private UsuarioServices usuarioService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, ExpiredJwtException, IOException {
    	System.out.println("\n ***********************************************************");
    	log.info("\t***** INICIA FILTER ***** ");
    	try{
	        final String token = getTokenFromRequest(request);
	        String username;
	        log.info("valida >>"+token);
	        if(token ==null) {
	        	log.info("Validacion de token no exitosa ");
	            filterChain.doFilter(request, response);
	            return;
	        }
	        
	        username = jwtService.getUserNameFromToken(token);
	        
	        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
	//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	        	UserDetails userDetails = usuarioService.getUser(username);
	        	if(userDetails==null) {
	        		response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"El usuario no existe");
	        		return;
	        	}
	            
	            if(jwtService.isTokenValid(token, userDetails.getUsername())) {
	                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	                
	                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                
	                SecurityContextHolder.getContext().setAuthentication(authToken);
	            }
	        }else {
	        	log.info("Se fue al else");
	        }
	        filterChain.doFilter(request, response);
    	}catch(ExpiredJwtException e) {
    		log.error(e.getMessage());
	        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
    	}catch(IOException e) {
    		e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
//    		throw new IOException(e);
    	}catch(ServletException e) {
    		e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
//    		throw new ServletException(e);
    	} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}finally {
	    	log.info("\t***** TERMINA FILTER ***** ");
	    	System.out.println("***********************************************************\n");
		}
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
        
        log.info("authHeader >> "+authHeader);
//        
//        Enumeration headerNames = request.getHeaderNames();
//        while(headerNames.hasMoreElements()) {
//          String headerName = (String)headerNames.nextElement();
//          System.out.println(headerName+" ["+ request.getHeader(headerName)+"]");
//        }
        
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        
        return null;
    }

}
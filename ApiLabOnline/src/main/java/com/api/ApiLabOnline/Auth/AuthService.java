package com.api.ApiLabOnline.Auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.entity.Usuario;
import com.api.ApiLabOnline.jwt.JwtService;
import com.api.ApiLabOnline.services.UsuarioServices;
import com.api.ApiLabOnline.utils.Utils;

import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthService {
	@Autowired
	private JwtService jwtService;
//	@Autowired
//	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
//	@Autowired
//	private AuthenticationManager authenticationManager; 
	@Autowired
	private UsuarioServices usuarioService;
	
	private Logger log = LogManager.getLogger(this.getClass());

	public AuthResponse login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Login "+request.toString());
		Usuario usuario =usuarioService.findByUsuariocorreo(loginRequest.getUsername());
		if(usuario==null || !passwordEncoder.matches(loginRequest.getPassword(), usuario.getUsuariopwd())) {
			throw new UsernameNotFoundException("El usuario o la contrasenia es incorrecta");
//			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED,"El usuario no existe");
//			throw new ServletException("No existe el usuario");
		}
		
//		log.info("1 >>("+usuario.getUsuariopwd()+")==("+loginRequest.getPassword()+") :: "
//		+passwordEncoder.matches(usuario.getUsuariopwd(), loginRequest.getPassword()));
//		log.info("2 >>("+usuario.getUsuariopwd()+")==("+passwordEncoder.encode(loginRequest.getPassword())+") :: "
//		+passwordEncoder.matches(usuario.getUsuariopwd(), passwordEncoder.encode(loginRequest.getPassword())));
//		
//		log.info("3 >>("+loginRequest.getPassword()+")==("+usuario.getUsuariopwd()+") :: "
//		+passwordEncoder.matches(loginRequest.getPassword(), usuario.getUsuariopwd()));
//		log.info("4 >>("+passwordEncoder.encode(loginRequest.getPassword())+")==("+usuario.getUsuariopwd()+") :: "
//		+passwordEncoder.matches(passwordEncoder.encode(loginRequest.getPassword()),usuario.getUsuariopwd()));
		
		AuthResponse res = AuthResponse.builder()
				.usuario(usuario)
				.token(jwtService.getToken(usuario))
				.refreshtoken(jwtService.getRefreshToken(usuario))
				.build();
		log.info("Get User >> "+usuario.toString());
		log.info("Return>>",res.toString());
		return res;
	}

	public AuthResponse update(ChangeRequest request) {
		log.info("Update "+request.toString());
		Usuario user =usuarioService.findByUsuariocorreo(request.getUsername());
		user.setUsuariopwd(passwordEncoder.encode(request.getPasswordnew()));
		log.info("Update >> "+user.toString());
		
		usuarioService.save(user);
		
		return AuthResponse.builder()
				.usuario(user)
				.token(jwtService.getToken(user))
				.build();
	}

	public AuthResponse register(Usuario usuario) {
		log.info("Register "+usuario.toString());
		usuario.setUsuarioactivo(true);
		usuario.setUsuariopwd(passwordEncoder.encode(usuario.getUsuariopwd()));
		usuario.setUsuariofechacreacion(Utils.getFecha());
		usuario.setUsuariofechamodificacion(Utils.getFecha());
		usuario.setUsuarioultimoacceso(Utils.getFecha());
		usuario.setUsuariokey("");
		
		usuarioService.save(usuario);
		
		return AuthResponse.builder()
				.usuario(usuario)
				.token(jwtService.getToken(usuario))
				.refreshtoken(jwtService.getRefreshToken(usuario))
				.build();
	}

	public AuthResponse refresh(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
        	token= token.substring(7);
        } else {
        	log.info("Validacion de token no exitosa ");
        }
        String username = jwtService.getUserNameFromToken(token);
        if(username==null) {
    		throw new UsernameNotFoundException("No se recupero el usuario.");
//			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED,"El usuario no existe");
//			throw new ServletException("No existe el usuario");
		}
        
		Usuario usuario =usuarioService.findByUsuariocorreo(username);

        if(usuario==null)
        	new UsernameNotFoundException("No se encontro el usuario.");

//        if(SecurityContextHolder.getContext().getAuthentication()==null) {
            
            if(jwtService.isTokenValid(token, usuario.getUsuariocorreo())) {
            	AuthResponse res = AuthResponse.builder()
        				.usuario(usuario)
        				.token(jwtService.getToken(usuario))
        				.refreshtoken(jwtService.getRefreshToken(usuario))
        				.build();
        		log.info("Get User >> "+usuario.toString());
        		log.info("Return>>",res.toString());
        		return res;
            }else {
            	log.info("Token invalido");
            }
//        }else {
//        	log.info("No se valido el usuario "+username);
//        }
		return null;
	}

}
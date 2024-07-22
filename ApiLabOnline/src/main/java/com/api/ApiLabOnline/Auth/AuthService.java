package com.api.ApiLabOnline.Auth;

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

	public AuthResponse login(LoginRequest request) {
		log.info("Login "+request.toString());
//		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		Usuario usuario =usuarioService.findByUsuariocorreo(request.getUsername());
		if(usuario==null)
			new UsernameNotFoundException("No se encontro el usuario.");
		
		String token = jwtService.getToken(usuario);
		AuthResponse res = AuthResponse.builder()
				.usuario(usuario)
				.token(token)
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
				.token(jwtService.getToken(usuario))
				.build();
	}

}
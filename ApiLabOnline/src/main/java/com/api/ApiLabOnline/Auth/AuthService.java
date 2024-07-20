package com.api.ApiLabOnline.Auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.entity.Usuario;
import com.api.ApiLabOnline.jwt.JwtService;
import com.api.ApiLabOnline.jwt.Role;
import com.api.ApiLabOnline.jwt.User;
import com.api.ApiLabOnline.repository.UserRepository;
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
		
		System.out.println("Recupero "+usuario.toString());
		
		String token = jwtService.getToken(usuario);
		AuthResponse res = AuthResponse.builder()
				.id(usuario.getUsuarioid())
				.username(usuario.getUsuariocorreo())
				.firstname(usuario.getUsuarionombre())
				.lastname(usuario.getUsuarioapellidopaterno()+" "+usuario.getUsuarioapellidomaterno())
				.country("country")
				.perfilid(usuario.getPerfilid())
				.token(token)
				.build();
		log.info("Get User >> "+usuario.toString());
		log.info("Return>>",res);
		return res;
	}

	public AuthResponse update(ChangeRequest request) {
		log.info("Update "+request.toString());
		Usuario user =usuarioService.findByUsuariocorreo(request.getUsername());
		user.setUsuariopwd(passwordEncoder.encode(request.getPasswordnew()));
		log.info("Update >> "+user.toString());
		
		usuarioService.save(user);
		
		return AuthResponse.builder()
				.token(jwtService.getToken(user))
				.build();
	}

	public AuthResponse register(RegisterRequest request) {
		log.info("Register "+request.toString());
		Usuario user = Usuario.builder()
				.usuarioactivo(true)
				.usuariocorreo(request.getUsername())
				.usuariopwd(passwordEncoder.encode(request.getPassword()))
				.perfilid(request.getPerfilid())
				.usuarionombre(request.getFirstname())
				.usuarioapellidopaterno(request.getLastname())
				.usuarioapellidomaterno("")
				.colaboradorid(-1)
				.usuariofechacreacion(Utils.getFecha())
				.usuariofechamodificacion(Utils.getFecha())
				.usuarioultimoacceso(Utils.getFecha())
				.usuariokey("")
				.usuarioimage(null)
				.build();
//		.country(request.getCountry())
		log.info("Save >> "+user.toString());
		
		usuarioService.save(user);
		
		return AuthResponse.builder()
				.token(jwtService.getToken(user))
				.build();
	}

}
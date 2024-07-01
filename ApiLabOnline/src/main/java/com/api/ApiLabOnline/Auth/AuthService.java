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

import com.api.ApiLabOnline.jwt.JwtService;
import com.api.ApiLabOnline.jwt.Role;
import com.api.ApiLabOnline.jwt.User;
import com.api.ApiLabOnline.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager; 
	
	private Logger log = LogManager.getLogger(this.getClass());

	public AuthResponse login(LoginRequest request) {
		log.info("Login "+request.toString());
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		User user =userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("No se encontro el usuario."));
		
		System.out.println("Recupero "+user.toString());
		
		String token = jwtService.getToken(user);
		AuthResponse res = AuthResponse.builder()
				.id(user.getId())
				.username(user.getUsername())
				.firstname(user.getFirstname())
				.lastname(user.getLastname())
				.country(user.getCountry())
				.role(user.getRole())
				.token(token)
				.build();
		log.info("Get User >> "+user.toString());
		return res;
	}

	public AuthResponse update(ChangeRequest request) {
		log.info("Update "+request.toString());
		User user =userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("No se encontro el usuario."));
		user.setPassword(passwordEncoder.encode(request.getPasswordnew()));
		log.info("Update >> "+user.toString());
		
		userRepository.save(user);
		
		return AuthResponse.builder()
				.token(jwtService.getToken(user))
				.build();
	}

	public AuthResponse register(RegisterRequest request) {
		log.info("Register "+request.toString());
		User user = User.builder()
				.username(request.getUsername())
				.password(passwordEncoder.encode(request.getPassword()))
				.firstname(request.getFirstname())
				.lastname(request.getLastname())
				.country(request.getCountry())
				.role(request.getRole())
				.build();
		log.info("Save >> "+user.toString());
		
		userRepository.save(user);
		
		return AuthResponse.builder()
				.token(jwtService.getToken(user))
				.build();
	}

}
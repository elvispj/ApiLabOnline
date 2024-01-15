package com.api.ApiLabOnline.Auth;

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

	public AuthResponse login(LoginRequest request) {
		System.out.println("Login "+request.toString());
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		UserDetails user =userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("No se encontro el usuario."));
		String token = jwtService.getToken(user);
		return AuthResponse.builder()
				.token(token)
				.build();
	}

	public AuthResponse register(RegisterRequest request) {
		System.out.println("Register "+request.toString());
		User user = User.builder()
				.username(request.getUsername())
				.password(passwordEncoder.encode(request.getPassword()))
				.firstname(request.getFirstname())
				.lastname(request.getLastname())
				.country(request.getCountry())
				.role(Role.USER)
				.build();
		System.out.println("Usuario nuevo "+user.toString());
		
		userRepository.save(user);
		
		return AuthResponse.builder()
				.token(jwtService.getToken(user))
				.build();
	}

}
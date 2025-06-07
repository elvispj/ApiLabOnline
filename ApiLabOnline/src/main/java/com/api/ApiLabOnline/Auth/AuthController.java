package com.api.ApiLabOnline.Auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.ApiLabOnline.entity.Usuario;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@CrossOrigin("")
@RequiredArgsConstructor
public class AuthController {
	@Autowired
	private AuthService authService;
	
	@PostMapping("login")
	public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return ResponseEntity.ok(authService.login(loginRequest, request, response));
	}
	
	@PostMapping("register")
	public ResponseEntity<AuthResponse> register(@RequestBody Usuario request) {
		return ResponseEntity.ok(authService.register(request));
	}
	
	@PostMapping("update")
	public ResponseEntity<AuthResponse> update(@RequestBody ChangeRequest request) {
		return ResponseEntity.ok(authService.update(request));
	}
	
	@PostMapping("refresh")
	public ResponseEntity<AuthResponse> refresh(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("\n\t\t ********* REFRESH TOKEN ********* ");
		return ResponseEntity.ok(authService.refresh(request,response));
	}
}
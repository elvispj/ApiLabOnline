package com.api.ApiLabOnline.Auth;

import com.api.ApiLabOnline.jwt.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
	int id;
	String token;
	String username;
	String firstname;
	String lastname;
	String country;
	Role role;
}
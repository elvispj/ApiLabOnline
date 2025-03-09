package com.api.ApiLabOnline.Auth;

import com.api.ApiLabOnline.entity.Usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
	Usuario usuario;
	String token;
	String refreshtoken;
}
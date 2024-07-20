package com.api.ApiLabOnline.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.entity.Perfil;
import com.api.ApiLabOnline.repository.PerfilRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PerfilServices {
	@Autowired
	private PerfilRepository perfilRepository;
	
	public Perfil findByPerfilid(int perfilid) {
		return perfilRepository.findByPerfilid(perfilid);
	}

}

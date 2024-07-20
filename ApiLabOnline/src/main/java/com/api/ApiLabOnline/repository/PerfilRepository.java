package com.api.ApiLabOnline.repository;

import com.api.ApiLabOnline.entity.Perfil;

public interface PerfilRepository {
	
	Perfil findByPerfilid(int perfilid);
	
	Perfil savePerfil(Perfil perfil);
	
	Perfil listPerfil();
}

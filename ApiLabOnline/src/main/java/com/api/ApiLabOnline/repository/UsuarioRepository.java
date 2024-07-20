package com.api.ApiLabOnline.repository;

import com.api.ApiLabOnline.entity.Usuario;

public interface UsuarioRepository {

	Usuario findByUsuariocorreo(String usuariocorreo);
	
	Usuario findByUsuarioid(int usuarioid);

	Usuario save(Usuario user);
}

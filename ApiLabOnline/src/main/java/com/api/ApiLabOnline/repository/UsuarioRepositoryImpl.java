package com.api.ApiLabOnline.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.api.ApiLabOnline.entity.Usuario;

@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public UsuarioRepositoryImpl(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate=jdbcTemplate;
	}

	@Override
	public Usuario findByUsuariocorreo(String usuariocorreo) {
		Object[] parameters = {usuariocorreo};
		List<Usuario> usuarios = jdbcTemplate.query("select * from usuario where usuarioactivo is true and usuariocorreo=?", 
				new BeanPropertyRowMapper<Usuario>(Usuario.class), parameters);
		return usuarios.get(0);
	}

	@Override
	public Usuario findByUsuarioid(int usuarioid) {
		Object[] parameters = {usuarioid};
		List<Usuario> usuarios = jdbcTemplate.query("select * from usuario where usuarioid=?", 
				new BeanPropertyRowMapper<Usuario>(Usuario.class), parameters);
		return usuarios.get(0);
	}
}

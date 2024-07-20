package com.api.ApiLabOnline.repository;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.api.ApiLabOnline.entity.Perfil;

@Repository
public class PerfilRepositoryImpl implements PerfilRepository {
	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public PerfilRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate=jdbcTemplate;
	}

	@Override
	public Perfil findByPerfilid(int perfilid) {
		log.debug("Search perfilid-"+perfilid);
		return jdbcTemplate.query("select * from perfil where perfilid=?", 
				new BeanPropertyRowMapper<Perfil>(Perfil.class), perfilid).get(0);
//		return jdbcTemplate.queryForObject("select * from perfil where perfilid=?", Perfil.class,perfilid);
	}

	@Override
	public Perfil savePerfil(Perfil perfil) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Perfil listPerfil() {
		// TODO Auto-generated method stub
		return null;
	}

}

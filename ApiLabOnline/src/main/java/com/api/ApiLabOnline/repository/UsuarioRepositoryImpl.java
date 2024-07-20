package com.api.ApiLabOnline.repository;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.api.ApiLabOnline.entity.Usuario;

@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {
	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public UsuarioRepositoryImpl(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate=jdbcTemplate;
	}

	@Override
	public Usuario findByUsuariocorreo(String usuariocorreo) {
		log.info("find by usuariocorreo-"+usuariocorreo);
		return jdbcTemplate.query("select * from usuarios where usuarioactivo is true and usuariocorreo=?", 
				new BeanPropertyRowMapper<Usuario>(Usuario.class), usuariocorreo).get(0);
//		jdbcTemplate.query("select * from usuario where usuarioactivo is true and usuariocorreo=?", 
//				new BeanPropertyRowMapper<Usuario>(Usuario.class), parameters);
	}

	@Override
	public Usuario findByUsuarioid(int usuarioid) {
		log.info("find by usuarioid-"+usuarioid);
		return jdbcTemplate.query("select * from usuarios where usuarioid=?", 
				new BeanPropertyRowMapper<Usuario>(Usuario.class), usuarioid).get(0);
//		return jdbcTemplate.queryForObject("select * from usuarios where usuarioid=?", Usuario.class,usuarioid);
	}

	@Override
	public Usuario save(Usuario usuario) {
		Object[] parameters = {usuario.getPerfilid(), usuario.getColaboradorid(), usuario.isUsuarioactivo(), 
				usuario.getUsuariocorreo(), usuario.getUsuariopwd(), usuario.getUsuarionombre(), 
				usuario.getUsuarioapellidopaterno(), usuario.getUsuarioapellidomaterno(), 
				usuario.getUsuariofechamodificacion(), usuario.getUsuarioultimoacceso(), 
				usuario.getUsuariokey(), usuario.getUsuarioimage(), usuario.getUsuarioid()};
		jdbcTemplate.update("UPDATE usuarios SET perfilid=?, colaboradorid=?, usuarioactivo=?, usuariocorreo=?, "
				+"usuariopwd=?, usuarionombre=?', usuarioapellidopaterno=?', usuarioapellidomaterno=?, usuariofechamodificacion=?::timestamp, "
				+"usuarioultimoacceso=?::timestamp, usuariokey=?, usuarioimage=? WHERE usuarioid=?;", parameters);
		return usuario;
	}
}

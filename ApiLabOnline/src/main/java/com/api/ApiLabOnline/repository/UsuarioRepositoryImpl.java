package com.api.ApiLabOnline.repository;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.api.ApiLabOnline.entity.Usuario;

@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {
	private Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public UsuarioRepositoryImpl(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate=jdbcTemplate;
	}

	@Override
	public Usuario findByUsuariocorreo(String usuariocorreo) {
		try {
			log.info("find by usuariocorreo-"+usuariocorreo);
			List<Usuario> usuario = jdbcTemplate.query("select * from usuarios where usuarioactivo is true and usuariocorreo=?", 
					new BeanPropertyRowMapper<Usuario>(Usuario.class), usuariocorreo);
			if(usuario!=null && usuario.size()>0)
				return usuario.get(0);
	    	log.info("No encontro informacion del usuario");
	    } catch (EmptyResultDataAccessException e) {
	    	e.printStackTrace();
	    	log.info("No encontro informacion del usuario");
	    }
        return null;
	}

	@Override
	public Usuario findByUsuarioid(int usuarioid) {
		log.info("find by usuarioid-"+usuarioid);
		return jdbcTemplate.query("select * from usuarios where usuarioid=?", 
				new BeanPropertyRowMapper<Usuario>(Usuario.class), usuarioid).get(0);
	}

	@Override
	public Usuario save(Usuario usuario){
		Object[] parameters;
		if(usuario.getUsuarioid()>0){
			log.info("update >> "+usuario.toString());
			parameters= new Object[]{usuario.getPerfilid(), usuario.getColaboradorid(), usuario.isUsuarioactivo(), 
					usuario.getUsuariocorreo(), usuario.getUsuariopwd(), usuario.getUsuarionombre(), 
					usuario.getUsuarioapellidopaterno(), usuario.getUsuarioapellidomaterno(), 
					usuario.getUsuariofechamodificacion(), usuario.getUsuarioultimoacceso(), 
					usuario.getUsuariokey(), usuario.getUsuarioimage(), usuario.getUsuarioid()};
			jdbcTemplate.update("UPDATE usuarios SET perfilid=?, colaboradorid=?, usuarioactivo=?, usuariocorreo=?, "
					+"usuariopwd=?, usuarionombre=?, usuarioapellidopaterno=?, usuarioapellidomaterno=?, usuariofechamodificacion=?::timestamp, "
					+"usuarioultimoacceso=?::timestamp, usuariokey=?, usuarioimage=? WHERE usuarioid=?;", parameters);
		} else {
			usuario.setUsuarioid(jdbcTemplate.queryForObject("SELECT nextval('usuariosid_seq') as id;", Integer.class));
			log.info("insert >> "+usuario.toString());
			parameters= new Object[]{usuario.getUsuarioid(),usuario.getPerfilid(), usuario.getColaboradorid()<1?null:usuario.getColaboradorid(), usuario.isUsuarioactivo(), 
					usuario.getUsuariocorreo(), usuario.getUsuariopwd(), usuario.getUsuarionombre(), usuario.getUsuarioapellidopaterno(), 
					usuario.getUsuarioapellidomaterno(), usuario.getUsuariofechacreacion(), usuario.getUsuariofechamodificacion(), 
					usuario.getUsuarioultimoacceso(), usuario.getUsuariokey(), usuario.getUsuarioimage()};
			jdbcTemplate.update("INSERT INTO public.usuarios(usuarioid, perfilid, colaboradorid, usuarioactivo, usuariocorreo, "
					+"usuariopwd, usuarionombre, usuarioapellidopaterno, usuarioapellidomaterno, usuariofechacreacion, "
					+"usuariofechamodificacion, usuarioultimoacceso, usuariokey, usuarioimage) "
					+"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?::timestamp, ?::timestamp, ?::timestamp, ?, ?);",parameters);
		}
		return usuario;
	}
}

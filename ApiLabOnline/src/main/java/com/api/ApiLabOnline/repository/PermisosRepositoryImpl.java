package com.api.ApiLabOnline.repository;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Repository
public class PermisosRepositoryImpl implements PermisosRepository {
	private Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    public PermisosRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

	@Override
	public JsonArray all() {
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from permisos where permisoactivo is true order by 1 ", new JsonObjectRowMapper());
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonObject findByUsuaioId(Long usuarioid) {
		try {
			return jdbcTemplate.queryForObject("SELECT po.* FROM usuarios u JOIN permisos p USING(perfilid) "
					+ "JOIN procesos po using(procesoid) WHERE permisoactivo is true and usuarioid=?",new JsonObjectRowMapper(), usuarioid);
	    } catch (EmptyResultDataAccessException e) {
	    	log.info("NO encontro informacion");
	        return null;
	    }
	}

	@Override
	public JsonObject findById(Long permisoid) {
		try {
			return jdbcTemplate.queryForObject("select * FROM permisos WHERE permisoid=?",new JsonObjectRowMapper(), permisoid);
	    } catch (EmptyResultDataAccessException e) {
	    	log.info("NO encontro informacion");
	        return null;
	    }
	}

	@Override
	public JsonObject save(String jsonPermiso) {
		JsonObject permiso = new Gson().fromJson(jsonPermiso, JsonObject.class);
		
//		permiso.addProperty("tipoestudioid", jdbcTemplate.queryForObject("SELECT nextval('tipoestudios_tipoestudioid_seq') as id;", Long.class));
//		
//		log.debug(permiso.toString());
//
//		Object[] parametros = {permiso.get("tipoestudioid").getAsLong(), permiso.get("tipoestudioactivo").getAsBoolean(), permiso.get("tipoestudionombre").getAsString(), 
//				permiso.get("tipoestudiodescripcion").getAsString(), permiso.get("tipoestudiofechacreacion").getAsString(), 
//				permiso.get("tipoestudiofechamodificacion").getAsString(), permiso.get("bitacoraid").getAsInt()};
//		jdbcTemplate.update("insert into tipoestudios(tipoestudioid, tipoestudioactivo, tipoestudionombre, "
//				+"tipoestudiodescripcion, tipoestudiofechacreacion, tipoestudiofechamodificacion, bitacoraid) "
//				+ "VALUES(?, ?, ?, ?, cast(? as timestamp), cast(? as timestamp), ?);", parametros);
//		log.debug("Se registro exitosamente "+permiso.get("tipoestudioid").getAsLong());
		return permiso;
	}

}

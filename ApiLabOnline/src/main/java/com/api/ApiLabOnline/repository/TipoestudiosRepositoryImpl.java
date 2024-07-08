package com.api.ApiLabOnline.repository;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Repository
public class TipoestudiosRepositoryImpl implements TipoestudiosRepository {
	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    public TipoestudiosRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

	@Override
	public JsonArray all() {
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from tipoestudios where tipoestudioactivo is true order by tipoestudionombre ", new JsonObjectRowMapper());
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonArray list(int limit, int offset) {
		JsonArray lista = new JsonArray();
		Object[] parameters = {limit, offset};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from tipoestudios order by 1 desc limit ? offset ?", new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonObject findById(Long tipoestudioid) {
		try {
			return jdbcTemplate.queryForObject("select * FROM tipoestudios WHERE tipoestudioid=?",new JsonObjectRowMapper(), tipoestudioid);
	    } catch (EmptyResultDataAccessException e) {
	    	log.info("NO encontro informacion");
	        return null;
	    }
	}

	@Override
	public JsonObject save(String jsonTipoestudio) {
		JsonObject tipoestudio = new Gson().fromJson(jsonTipoestudio, JsonObject.class);
		
		tipoestudio.addProperty("tipoestudioid", jdbcTemplate.queryForObject("SELECT nextval('tipoestudios_tipoestudioid_seq') as id;", Long.class));
		
		log.debug(tipoestudio.toString());

		Object[] parametros = {tipoestudio.get("tipoestudioid").getAsLong(), tipoestudio.get("tipoestudioactivo").getAsBoolean(), tipoestudio.get("tipoestudionombre").getAsString(), 
				tipoestudio.get("tipoestudiodescripcion").getAsString(), tipoestudio.get("tipoestudiofechacreacion").getAsString(), 
				tipoestudio.get("tipoestudiofechamodificacion").getAsString(), tipoestudio.get("bitacoraid").getAsInt()};
		jdbcTemplate.update("insert into tipoestudios(tipoestudioid, tipoestudioactivo, tipoestudionombre, "
				+"tipoestudiodescripcion, tipoestudiofechacreacion, tipoestudiofechamodificacion, bitacoraid) "
				+ "VALUES(?, ?, ?, ?, cast(? as timestamp), cast(? as timestamp), ?);", parametros);
		log.debug("Se registro exitosamente "+tipoestudio.get("tipoestudioid").getAsLong());
		return tipoestudio;
	}

}

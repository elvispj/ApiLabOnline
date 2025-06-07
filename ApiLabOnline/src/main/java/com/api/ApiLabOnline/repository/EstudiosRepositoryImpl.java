package com.api.ApiLabOnline.repository;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.api.ApiLabOnline.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Repository
public class EstudiosRepositoryImpl implements EstudiosRepository {
	private Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    public EstudiosRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

	@Override
	public JsonArray getAll() {
		log.debug("Buscar todos");
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from estudios where estudioactivo is true order by 1 desc", 
				new JsonObjectRowMapper());
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonArray listEstudios(int limit, int offset) {
		log.debug("Buscar todos limit["+limit+"] offset["+offset+"]");
		JsonArray lista = new JsonArray();
		Object[] parameters = {limit, offset};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from estudios where estudioactivo is true order by 1 desc limit ? offset ?", new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonObject findById(Long id) {
		log.debug("Buscar id-"+id);
		try {
			return jdbcTemplate.queryForObject("select * FROM estudios WHERE estudioid=?", new JsonObjectRowMapper(), id);
	    } catch (EmptyResultDataAccessException e) {
	    	log.info("NO encontro informacion");
	        return null;
	    }
	}

	@Override
	public JsonObject save(String estudio) {
		log.debug("Guardado \n"+estudio.toString());
		JsonObject jsonEstudio = new Gson().fromJson(estudio, JsonObject.class);
		
		jsonEstudio.addProperty("estudioid", jdbcTemplate.queryForObject("SELECT nextval('estudios_estudioid_seq') as id;", Long.class));
		jsonEstudio.addProperty("estudioactivo", true);
		jsonEstudio.addProperty("estudiofechacreacion", Utils.getFechaActual());
		jsonEstudio.addProperty("estudiofechamodificacion", Utils.getFechaActual());
		jsonEstudio.addProperty("bitacoraid", -1);
		Object[] parametros = {
				jsonEstudio.get("estudioid").getAsInt(), jsonEstudio.get("tipoestudioid").getAsInt(), 
				jsonEstudio.get("estudioactivo").getAsBoolean(), jsonEstudio.get("estudionombre").getAsString(), 
				jsonEstudio.get("estudiodescripcion").getAsString(), jsonEstudio.get("estudiofechacreacion").getAsString(), 
				jsonEstudio.get("estudiofechamodificacion").getAsString(), jsonEstudio.get("bitacoraid").getAsInt(), 
				jsonEstudio.get("estudionombrecorto").getAsString(), jsonEstudio.get("estudiocosto").getAsFloat()};
		jdbcTemplate.update("INSERT INTO estudios(estudioid, tipoestudioid, estudioactivo, estudionombre, estudiodescripcion, "
				+ "estudiofechacreacion, estudiofechamodificacion, bitacoraid, estudionombrecorto, estudiocosto) "
				+"VALUES(?, ?, ?, ?, ?, cast(? as timestamp), cast(? as timestamp), ?, ?, ? )", parametros);
		
		return jsonEstudio;
	}

	@Override
	public JsonObject update(String estudio) {
		log.debug("Actualizando \n"+estudio.toString());
		JsonObject jsonEstudio = new Gson().fromJson(estudio, JsonObject.class);
		jsonEstudio.addProperty("estudiofechamodificacion", Utils.getFechaActual());
		Object[] parametros = {
				jsonEstudio.get("estudioactivo").getAsBoolean(), jsonEstudio.get("estudionombre").getAsString(), 
				jsonEstudio.get("estudiodescripcion").getAsString(), jsonEstudio.get("estudionombrecorto").getAsString(), 
				jsonEstudio.get("estudiocosto").getAsNumber(), jsonEstudio.get("estudiofechamodificacion").getAsString(), 				
				jsonEstudio.get("estudioid").getAsInt()};
		jdbcTemplate.update("update estudios set estudioactivo=?, estudionombre=?, estudiodescripcion=?, "
				+"estudionombrecorto=?, estudiocosto=?, estudiofechamodificacion=cast(? as timestamp) where estudioid=?", parametros);
		
		return jsonEstudio;
	}

	@Override
	public int deleteById(Long id) {
		log.debug("Eliminado id-"+id);
		Object[] parametros = {id};
	    return jdbcTemplate.update("DELETE FROM estudios WHERE estudioid=? and 1=2", parametros);
	}
	
}

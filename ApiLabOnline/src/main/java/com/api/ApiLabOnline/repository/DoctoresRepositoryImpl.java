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
public class DoctoresRepositoryImpl implements DoctoresRepository {
	private Logger log = LogManager.getLogger(DoctoresRepositoryImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public DoctoresRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

	@Override
	public JsonArray all() {
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from doctores where doctoractivo is true order by doctornombre ", new JsonObjectRowMapper());
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonArray list(int limit, int offset) {
		JsonArray lista = new JsonArray();
		Object[] parameters = {limit, offset};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from doctores order by 1 desc limit ? offset ?", new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonObject findById(Long doctorid) {
		try {
			return jdbcTemplate.queryForObject("select * FROM doctores WHERE doctorid=?",new JsonObjectRowMapper(), doctorid);
	    } catch (EmptyResultDataAccessException e) {
	    	log.info("NO encontro informacion del doctor");
	        return null;
	    }
	}

	@Override
	public JsonObject findByUsuarioId(Long usuarioid) {
		try {
			return jdbcTemplate.queryForObject("select * FROM doctores WHERE doctoractivo is true and usuarioid=?",new JsonObjectRowMapper(), usuarioid);
	    } catch (EmptyResultDataAccessException e) {
	    	log.info("NO encontro informacion del doctor");
	        return null;
	    }
	}

	@Override
	public JsonObject update(String jsonDoctor) {
		JsonObject doctor = new Gson().fromJson(jsonDoctor, JsonObject.class);

		doctor.addProperty("doctorfechamodificacion", Utils.getFechaActual());

		Object[] parametros = {doctor.get("doctoractivo").getAsBoolean(), doctor.get("doctornombre").getAsString(), doctor.get("doctorapellidopaterno").getAsString(), 
				doctor.get("doctorapellidomaterno").getAsString(), doctor.get("doctorcedula").getAsString(), doctor.get("doctortitulo").getAsString(), 
				doctor.get("usuariopref").getAsString(), doctor.get("doctorfechamodificacion").getAsString(), doctor.get("doctorid").getAsInt()};
		jdbcTemplate.update("update doctores set doctoractivo=?, doctornombre=?, doctorapellidopaterno=?, doctorapellidomaterno=?, "
				+ "doctorcedula=?, doctortitulo=?, usuariopref=?, doctorfechamodificacion=cast(? as timestamp) where doctorid=? ", parametros);
		
		log.info("Se actualizo "+doctor.toString());
		return doctor;
	}

	@Override
	public JsonObject save(String jsonDoctor) {
		JsonObject doctor = new Gson().fromJson(jsonDoctor, JsonObject.class);
		
		doctor.addProperty("doctorid", jdbcTemplate.queryForObject("SELECT nextval('doctores_doctorid_seq') as id;", Long.class));
		doctor.addProperty("doctoractivo", true);
		doctor.addProperty("doctorfechacreacion", Utils.getFechaActual());
		doctor.addProperty("doctorfechamodificacion", Utils.getFechaActual());
		doctor.addProperty("bitacoraid", -1);

		Object[] parametros = {doctor.get("doctorid").getAsLong(), doctor.get("doctoractivo").getAsBoolean(), doctor.get("doctornombre").getAsString(), 
				doctor.get("doctorapellidopaterno").getAsString(), doctor.get("doctorapellidomaterno").getAsString(), 
				doctor.get("doctorcedula").getAsString(), doctor.get("doctortitulo").getAsString(), doctor.get("doctorfechacreacion").getAsString(), 
				doctor.get("doctorfechamodificacion").getAsString(), doctor.get("bitacoraid").getAsInt(), doctor.get("usuariopref").getAsString()};
		jdbcTemplate.update("INSERT INTO doctores(doctorid, doctoractivo, doctornombre, doctorapellidopaterno, doctorapellidomaterno, "
				+ "doctorcedula, doctortitulo, doctorfechacreacion, doctorfechamodificacion, bitacoraid, usuariopref) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, cast(? as timestamp), cast(? as timestamp), ?, ?);", parametros);
		log.info("Se registro exitosamente "+doctor.toString());
		return doctor;
	}

}

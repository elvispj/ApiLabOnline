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
public class CitasRepositoryImpl implements CitasRepository {
	private Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public CitasRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

	@Override
	public JsonArray all() {
		JsonArray lista = new JsonArray();
		Object[] parameters = {"CAN"};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from doctorcitas where citaestatusid!=? order by citafecha ", new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonArray list(int limit, int offset) {
		JsonArray lista = new JsonArray();
		Object[] parameters = {"CAN",limit, offset};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from doctorcitas where citaestatusid!=? order by 1 desc limit ? offset ?", new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonArray listByDoctorid(Long doctorid) {
		JsonArray lista = new JsonArray();
		Object[] parameters = {"CAN",doctorid};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from doctorcitas where citaestatusid!=? and doctorid=? order by citafecha desc", new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonArray listByClienteid(Long pacienteid) {
		JsonArray lista = new JsonArray();
		Object[] parameters = {"CAN",pacienteid};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from doctorcitas where citaestatusid!=? and pacienteid=? order by citafecha desc", new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonObject findById(Long citaid) {
		try {
			return jdbcTemplate.queryForObject("select * FROM doctorcitas WHERE citaid=?",new JsonObjectRowMapper(), citaid);
	    } catch (EmptyResultDataAccessException e) {
	    	log.info("No encontro informacion de la cita");
	        return null;
	    } catch(Exception ex) {
	    	ex.printStackTrace();
	    	return null;
	    }
	}

	@Override
	public JsonObject update(String jsonCita) {
		JsonObject cita = new Gson().fromJson(jsonCita, JsonObject.class);
		
		cita.addProperty("citafechamodificacion", Utils.getFechaActual());

		Object[] parametros = {cita.get("doctorid").getAsInt(), cita.get("pacienteid").getAsInt(), 
				cita.get("citaestatusid").getAsString(), cita.get("citanombre").getAsString(), cita.get("citafecha").getAsString(), 
				cita.get("citalugar").getAsString(), cita.get("citacomentarios").getAsString(), 
				cita.get("citafechamodificacion").getAsString(), cita.get("citaid").getAsLong() };
		jdbcTemplate.update("UPDATE doctorcitas SET doctorid=?, pacienteid=?, citaestatusid=?, citanombre=?, citafecha=?::timestamp, "
				+"citalugar=?, citacomentarios=?, citafechamodificacion=?::timestamp WHERE citaid=?;", parametros);
		log.info("Se actualizo exitosamente "+cita.toString());
		return cita;
	}

	@Override
	public JsonObject save(String jsonCita) {
		JsonObject cita = new Gson().fromJson(jsonCita, JsonObject.class);
		
		cita.addProperty("citaid", jdbcTemplate.queryForObject("SELECT nextval('doctorcitas_citaid_seq') as id;", Long.class));
		cita.addProperty("citafechacreacion", Utils.getFechaActual());
		cita.addProperty("citafechamodificacion", Utils.getFechaActual());

		Object[] parametros = {cita.get("citaid").getAsLong(), cita.get("doctorid").getAsInt(), cita.get("pacienteid").getAsInt(), 
				cita.get("citaestatusid").getAsString(), cita.get("citanombre").getAsString(), cita.get("citafecha").getAsString(), 
				cita.get("citalugar").getAsString(), cita.get("citacomentarios").getAsString(), 
				cita.get("citafechacreacion").getAsString(), cita.get("citafechamodificacion").getAsString() };
		jdbcTemplate.update("INSERT INTO doctorcitas (citaid, doctorid, pacienteid, citaestatusid, citanombre, "
				+"citafecha, citalugar, citacomentarios, citafechacreacion, citafechamodificacion) "
				+"VALUES(?, ?, ?, ?, ?, ?::timestamp, ?, ?, ?::timestamp, ?::timestamp);", parametros);
		log.info("Se registro exitosamente "+cita.toString());
		return cita;
	}

}

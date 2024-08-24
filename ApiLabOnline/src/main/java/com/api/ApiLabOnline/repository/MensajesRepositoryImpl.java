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
public class MensajesRepositoryImpl implements MensajesRepository {
	private Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public MensajesRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

	@Override
	public JsonArray listByDoctorid(Long doctorid) {
		JsonArray lista = new JsonArray();
		Object[] parameters = {"ELI",doctorid};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from mensajes where mensajeestatusid!=? and doctorid=? order by mensajefechacreacion desc", new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonArray listByClienteid(Long clienteid) {
		JsonArray lista = new JsonArray();
		Object[] parameters = {"ELI",clienteid};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from mensajes where mensajeestatusid!=? and clienteid=? order by mensajefechacreacion desc", new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonObject findById(Long mensajeid) {
		try {
			return jdbcTemplate.queryForObject("select * FROM mensajes WHERE mensajeid=?",new JsonObjectRowMapper(), mensajeid);
	    } catch (EmptyResultDataAccessException e) {
	    	log.info("No encontro informacion de la cita");
	        return null;
	    } catch(Exception ex) {
	    	ex.printStackTrace();
	    	return null;
	    }
	}

	@Override
	public JsonObject update(String jsonMensaje) {
		JsonObject mensaje = new Gson().fromJson(jsonMensaje, JsonObject.class);
		
		mensaje.addProperty("mensajefechamodificacion", Utils.getFechaActual());
		if(!mensaje.has("mensajerespuesta") || mensaje.get("mensajerespuesta")==null 
				|| mensaje.get("mensajerespuesta").getAsString().trim().length()<1) {
			mensaje.remove("mensajerespuesta");
			mensaje.addProperty("mensajerespuesta", "{}");
		}

		Object[] parametros = {mensaje.get("mensajetipoid").getAsString(),mensaje.get("mensajeestatusid").getAsString(),
				mensaje.get("doctorid").getAsInt(), mensaje.get("clienteid").getAsInt(), 
				mensaje.get("mensajetitulo").getAsString(),mensaje.get("mensajecuerpo").getAsString(),
				mensaje.get("mensajerespuesta").getAsString(),mensaje.get("mensajefechamodificacion").getAsString(),
				mensaje.get("mensajeid").getAsLong() };
		jdbcTemplate.update("UPDATE mensajes SET mensajetipoid=?, mensajeestatusid=?, doctorid=?, clienteid=?, "
				+"mensajetitulo=?, mensajecuerpo=?, mensajerespuesta=?::json, mensajefechamodificacion=?::timestamp WHERE mensajeid=?;", parametros);
		log.info("Se actualizo exitosamente "+mensaje.toString());
		return mensaje;
	}

	@Override
	public JsonObject save(String jsonMensaje) {
		JsonObject mensaje = new Gson().fromJson(jsonMensaje, JsonObject.class);
		
		mensaje.addProperty("mensajeid", jdbcTemplate.queryForObject("SELECT nextval('mensajes_mensajeid_seq') as id;", Long.class));
		mensaje.addProperty("mensajefechacreacion", Utils.getFechaActual());
		mensaje.addProperty("mensajefechamodificacion", Utils.getFechaActual());
		if(!mensaje.has("mensajerespuesta") || mensaje.get("mensajerespuesta")==null 
				|| mensaje.get("mensajerespuesta").getAsString().trim().length()<1) {
			mensaje.remove("mensajerespuesta");
			mensaje.addProperty("mensajerespuesta", "{}");
		}

		Object[] parametros = {mensaje.get("mensajeid").getAsLong(), mensaje.get("mensajetipoid").getAsString(), mensaje.get("mensajeestatusid").getAsString(),
				mensaje.get("doctorid").getAsInt(), mensaje.get("clienteid").getAsInt(), mensaje.get("mensajetitulo").getAsString(), 
				mensaje.get("mensajecuerpo").getAsString(), mensaje.get("mensajerespuesta").getAsString(), 
				mensaje.get("mensajefechacreacion").getAsString(), mensaje.get("mensajefechamodificacion").getAsString()};
		jdbcTemplate.update("INSERT INTO mensajes(mensajeid, mensajetipoid, mensajeestatusid, doctorid, clienteid, "
				+"mensajetitulo, mensajecuerpo, mensajerespuesta, mensajefechacreacion, mensajefechamodificacion); "
				+"VALUES(?, ?, ?, ?, ?, ?, ?, ?::json, ?::timestamp, ?::timestamp );", parametros);
		log.info("Se registro exitosamente "+mensaje.toString());
		return mensaje;
	}

}

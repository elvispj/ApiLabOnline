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
public class ProveedoresRepositoryImpl implements ProveedoresRepository {
	private Logger log = LogManager.getLogger(ProveedoresRepositoryImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public ProveedoresRepositoryImpl(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate=jdbcTemplate;
	}

	@Override
	public JsonArray getAll() {
	
		log.info("Buscar todos");
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from proveedores where proveedoractivo is true order by 1 desc", 
				new JsonObjectRowMapper());
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonArray list(int limit, int offset) {
		log.info("Buscar limit["+limit+"] offset["+offset+"]");
		JsonArray lista = new JsonArray();
		Object[] parameters = {limit, offset};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from proveedores where proveedoractivo is true order by 1 desc limit ? offset ?", 
				new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonObject findById(Long id) {
		log.info("Buscar id-"+id);
		try {
			return jdbcTemplate.queryForObject("select * FROM proveedores WHERE proveedorid=?", new JsonObjectRowMapper(), id);
	    } catch (EmptyResultDataAccessException e) {
	    	log.info("NO encontro informacion");
	        return null;
	    }
	}

	@Override
	public JsonObject update(String proveedores) {
		log.info("Actualizando \n"+proveedores.toString());
		JsonObject jsonproveedores = new Gson().fromJson(proveedores, JsonObject.class);
		jsonproveedores.addProperty("proveedorfechamodificacion", Utils.getFechaActual());
		Object[] parametros = {
				jsonproveedores.get("proveedoractivo").getAsBoolean(), jsonproveedores.get("proveedornombre").getAsString(), 
				jsonproveedores.get("proveedorfechamodificacion").getAsString(), jsonproveedores.get("proveedorid").getAsInt()};
		jdbcTemplate.update("update proveedores set proveedoractivo=?, proveedornombre=?, proveedorfechamodificacion=cast(? as timestamp) where proveedorid=?", parametros);
		
		return jsonproveedores;
	}

	@Override
	public JsonObject save(String proveedores) {
		log.info("Guardado \n"+proveedores.toString());
		JsonObject jsonProveedores = new Gson().fromJson(proveedores, JsonObject.class);
		
		jsonProveedores.addProperty("proveedorid", jdbcTemplate.queryForObject("SELECT nextval('proveedores_proveedorid_seq') as id;", Long.class));
		jsonProveedores.addProperty("proveedoractivo", true);
		jsonProveedores.addProperty("proveedorfechacreacion", Utils.getFechaActual());
		jsonProveedores.addProperty("proveedorfechamodificacion", Utils.getFechaActual());
		jsonProveedores.addProperty("bitacoraid", -1);
		Object[] parametros = {
				jsonProveedores.get("proveedorid").getAsString(), jsonProveedores.get("proveedoractivo").getAsString(), 
				jsonProveedores.get("proveedornombre").getAsString(), jsonProveedores.get("proveedorfechacreacion").getAsString(), 
				jsonProveedores.get("proveedorfechamodificacion").getAsString(), jsonProveedores.get("bitacoraid").getAsString()};
		jdbcTemplate.update("INSERT INTO proveedores(proveedorid, proveedoractivo, proveedornombre, proveedorfechacreacion, proveedorfechamodificacion, bitacoraid) "
				+"VALUES(?, ?, ?, cast(? as timestamp), cast(? as timestamp), ?)", parametros);
		
		return jsonProveedores;
	}

	@Override
	public int deleteById(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}
}

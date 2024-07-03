package com.api.ApiLabOnline.repository;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.api.ApiLabOnline.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Repository
public class PagosRepositoryImpl implements PagosRepository {
	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public PagosRepositoryImpl(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate=jdbcTemplate;
	}

	@Override
	public JsonArray getAll() {
	
		log.info("Buscar todos");
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from pagos where pagoactivo is true order by 1 desc", 
				new JsonObjectRowMapper());
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonArray list(int limit, int offset) {
		log.info("Buscar todos limit["+limit+"] offset["+offset+"]");
		JsonArray lista = new JsonArray();
		Object[] parameters = {limit, offset};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from pagos where pagoactivo is true order by 1 desc limit ? offset ?", new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonObject findById(Long id) {
	
		log.info("Buscar id-"+id);
		Object[] parametros = {id};
	    return jdbcTemplate.query("select * FROM pagos WHERE pagoid=?", new JsonObjectRowMapper(), parametros).get(0);
	}

	@Override
	public JsonObject update(String pagos) {
		log.info("Actualizando \n"+pagos.toString());
		JsonObject jsonPagos = new Gson().fromJson(pagos, JsonObject.class);
		jsonPagos.addProperty("pagofechamodificacion", Utils.getFechaActual());
		Object[] parametros = {
				jsonPagos.get("pagoestatusid").getAsBoolean(), jsonPagos.get("pagoimporte").getAsDouble(), 
				jsonPagos.get("pagoiva").getAsDouble(), jsonPagos.get("pagoimportetotal").getAsDouble(), 
				jsonPagos.get("pagofechamodificacion").getAsString(), jsonPagos.get("pagoid").getAsInt()};
		jdbcTemplate.update("update pagos set pagoestatusid=?, pagoimporte=?, pagoiva=?, "
				+"pagoimportetotal=?, pagofechamodificacion=cast(? as timestamp) where pagoid=?", parametros);
		return jsonPagos;
	}

	@Override
	public JsonObject save(String pago) {
		log.info("Guardado \n"+pago.toString());
		JsonObject jsonPagos = new Gson().fromJson(pago, JsonObject.class);
		
		jsonPagos.addProperty("pagoid", jdbcTemplate.queryForObject("SELECT nextval('pagos_pagoid_seq') as id;", Long.class));
		jsonPagos.addProperty("pagoestatusid", "PEN");
		jsonPagos.addProperty("pagofechacreacion", Utils.getFechaActual());
		jsonPagos.addProperty("pagofechamodificacion", Utils.getFechaActual());
//		jsonCompras.addProperty("bitacoraid", -1);
		Object[] parametros = {
				jsonPagos.get("pagoid").getAsInt(), jsonPagos.get("ordenid").getAsInt(), 
				jsonPagos.get("pagoestatusid").getAsString(), jsonPagos.get("pagoimporte").getAsDouble(), 
				jsonPagos.get("pagoiva").getAsDouble(), jsonPagos.get("pagoimportetotal").getAsDouble(), 
				jsonPagos.get("pagofechacreacion").getAsString(), jsonPagos.get("pagofechamodificacion").getAsString()};
		jdbcTemplate.update("INSERT INTO pagos(pagoid,ordenid,pagoestatusid,pagoimporte,pagoiva,"
				+"pagoimportetotal,pagofechacreacion,pagofechamodificacion) "
				+"VALUES(?, ?, ?, ?, ?, ?, cast(? as timestamp), cast(? as timestamp))", parametros);
		return jsonPagos;
	}

	@Override
	public int deleteById(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}
}

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
public class TipoProductosRepositoryImpl implements TipoProductosRepository {
	private Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public TipoProductosRepositoryImpl(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate=jdbcTemplate;
	}

	@Override
	public JsonArray getAll() {
		log.debug("Buscar todos");
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from tipoproducto where tipoproductoactivo is true order by 1 desc", 
				new JsonObjectRowMapper());
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonArray list(int limit, int offset) {
		log.debug("Buscar todos limit["+limit+"] offset["+offset+"]");
		JsonArray lista = new JsonArray();
		Object[] parameters = {limit, offset};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from tipoproducto where tipoproductoactivo is true order by 1 desc limit ? offset ?", new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonObject findById(Long id) {
		log.debug("Buscar id-"+id);
		try {
			return jdbcTemplate.queryForObject("select * FROM tipoproducto WHERE tipoproductoid=?", new JsonObjectRowMapper(), id);
	    } catch (EmptyResultDataAccessException e) {
	    	log.info("NO encontro informacion");
	        return null;
	    }
	}

	@Override
	public JsonObject update(String tipoproducto) {
		log.debug("Actualizando \n"+tipoproducto.toString());
		JsonObject jsonTipoProducto = new Gson().fromJson(tipoproducto, JsonObject.class);
		jsonTipoProducto.addProperty("tipoproductofechamodificacion", Utils.getFechaActual());
		Object[] parametros = {
				jsonTipoProducto.get("tipoproductoactivo").getAsBoolean(), jsonTipoProducto.get("tipoproductonombre").getAsString(), 
				jsonTipoProducto.get("tipoproductodescripcion").getAsString(), jsonTipoProducto.get("tipoproductofechamodificacion").getAsString(), 
				jsonTipoProducto.get("tipoproductoid").getAsInt()};
		jdbcTemplate.update("update tipoproducto set tipoproductoactivo=?, tipoproductonombre=?, "
				+"tipoproductodescripcion=?, tipoproductofechamodificacion=cast(? as timestamp) where tipoproductoid=?", parametros);
		
		return jsonTipoProducto;
	}

	@Override
	public JsonObject save(String tipoproducto) {
		log.debug("Guardado \n"+tipoproducto.toString());
		JsonObject jsonTipoProducto = new Gson().fromJson(tipoproducto, JsonObject.class);
		
		jsonTipoProducto.addProperty("tipoproductoid", jdbcTemplate.queryForObject("SELECT nextval('tipoproducto_tipoproductoid_seq') as id;", Long.class));
		jsonTipoProducto.addProperty("tipoproductoactivo", true);
		jsonTipoProducto.addProperty("tipoproductofechacreacion", Utils.getFechaActual());
		jsonTipoProducto.addProperty("tipoproductofechamodificacion", Utils.getFechaActual());
		jsonTipoProducto.addProperty("bitacoraid", -1);
		Object[] parametros = {
				jsonTipoProducto.get("tipoproductoid").getAsString(), jsonTipoProducto.get("tipoproductoactivo").getAsString(), 
				jsonTipoProducto.get("tipoproductonombre").getAsString(), jsonTipoProducto.get("tipoproductodescripcion").getAsString(), 
				jsonTipoProducto.get("tipoproductofechacreacion").getAsString(), jsonTipoProducto.get("tipoproductofechamodificacion").getAsString(),
				jsonTipoProducto.get("bitacoraid").getAsString()};
		jdbcTemplate.update("INSERT INTO tipoproducto(tipoproductoid,tipoproductoactivo,tipoproductonombre,"
				+"tipoproductodescripcion,tipoproductofechacreacion,tipoproductofechamodificacion,bitacoraid) "
				+"VALUES(?, ?, ?, ?, cast(? as timestamp), cast(? as timestamp), ?)", parametros);
		
		return jsonTipoProducto;
	}

	@Override
	public int deleteById(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}
}

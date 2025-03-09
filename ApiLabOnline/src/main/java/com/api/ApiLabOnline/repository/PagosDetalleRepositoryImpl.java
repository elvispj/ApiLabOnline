package com.api.ApiLabOnline.repository;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.api.ApiLabOnline.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Repository
public class PagosDetalleRepositoryImpl implements PagosDetalleRepository {
	private Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public PagosDetalleRepositoryImpl(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate=jdbcTemplate;
	}

	@Override
	public JsonArray getAll() {
	
		log.info("Buscar todos");
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from pagodetalle where pagodetalleactivo is true order by 1 desc", 
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
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from pagodetalle where pagodetalleactivo is true order by 1 desc limit ? offset ?", 
				new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonArray findByPagoId(Long pagoid) {
		log.info("Buscar pagoid-"+pagoid);
		Object[] parametros = {pagoid};
		JsonArray lista = new JsonArray();
		List<JsonObject> listaDetalle = jdbcTemplate.query("select * FROM pagodetalle WHERE pagodetalleactivo is true and pagoid=? order by pagodetalleid", 
				new JsonObjectRowMapper(), parametros);
		for(JsonObject jsonObjecto: listaDetalle) {
			lista.add(jsonObjecto);
		}
	    return lista;
	}

	@Override
	public JsonObject findById(Long id) {
		log.info("Buscar id-"+id);
		Object[] parametros = {id};
	    return jdbcTemplate.query("select * FROM pagodetalle WHERE pagodetalleid=?", new JsonObjectRowMapper(), parametros).get(0);
	}

	@Override
	public void update(String pagoDetalle) {
		log.info("Actualizando \n"+pagoDetalle.toString());
		JsonObject jsonPagosDetalle = new Gson().fromJson(pagoDetalle, JsonObject.class);
		jsonPagosDetalle.addProperty("pagodetallefechamodificacion", Utils.getFechaActual());
		Object[] parametros = {
				jsonPagosDetalle.get("pagodetalleactivo").getAsBoolean(), jsonPagosDetalle.get("pagodetalleimporte").getAsDouble(), 
				jsonPagosDetalle.get("pagodetallefechamodificacion").getAsString(), jsonPagosDetalle.get("pagodetalleid").getAsInt()};
		jdbcTemplate.update("update pagodetalle set pagodetalleactivo=?, pagodetalleimporte=?, "
				+"pagodetallefechamodificacion=cast(? as timestamp) where pagodetalleid=?", parametros);
	}

	@Override
	public JsonObject save(String pagodetalle) {
		log.info("Guardado \n"+pagodetalle.toString());
		JsonObject jsonPagoDetalle = new Gson().fromJson(pagodetalle, JsonObject.class);
		
		jsonPagoDetalle.addProperty("pagodetalleid", jdbcTemplate.queryForObject("SELECT nextval('pagodetalle_pagodetalleid_seq') as id;", Long.class));
		jsonPagoDetalle.addProperty("pagodetalleactivo", true);
		jsonPagoDetalle.addProperty("pagodetallefechacreacion", Utils.getFechaActual());
		jsonPagoDetalle.addProperty("pagodetallefechamodificacion", Utils.getFechaActual());
		jsonPagoDetalle.addProperty("bitacoraid", -1);
		Object[] parametros = {
				jsonPagoDetalle.get("pagodetalleid").getAsInt(), jsonPagoDetalle.get("pagoid").getAsInt(), 
				jsonPagoDetalle.get("movimientocajaid").getAsInt(), jsonPagoDetalle.get("pagodetalleactivo").getAsBoolean(), 
				jsonPagoDetalle.get("pagodetalleimporte").getAsDouble(), jsonPagoDetalle.get("pagodetallefechacreacion").getAsString(), 
				jsonPagoDetalle.get("pagodetallefechamodificacion").getAsString(), jsonPagoDetalle.get("bitacoraid").getAsInt()};
		jdbcTemplate.update("INSERT INTO pagodetalle(pagodetalleid,pagoid,movimientocajaid,pagodetalleactivo,"
				+"pagodetalleimporte,pagodetallefechacreacion,pagodetallefechamodificacion,bitacoraid) "
				+"VALUES(?, ?, ?, ?, ?, cast(? as timestamp), cast(? as timestamp), ?)", parametros);
		return jsonPagoDetalle;
	}

	@Override
	public int deleteById(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}
}

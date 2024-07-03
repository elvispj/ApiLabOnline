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
public class MovimientosCajaRepositoryImpl implements MovimientosCajaRepository {
	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public MovimientosCajaRepositoryImpl(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate=jdbcTemplate;
	}

	@Override
	public JsonArray getAll() {
	
		log.info("Buscar todos");
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from movimientoscaja order by 1 desc limit 1000", 
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
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from movimientoscaja order by 1 desc limit ? offset ?", new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonObject findById(Long id) {
	
		log.info("Buscar id-"+id);
		Object[] parametros = {id};
	    return jdbcTemplate.query("select * FROM movimientoscaja WHERE movimientoid=?", new JsonObjectRowMapper(), parametros).get(0);
	}

	@Override
	public JsonObject update(String movimientoscaja) {
		log.info("Actualizando \n"+movimientoscaja.toString());
		JsonObject jsonMovientosCaja = new Gson().fromJson(movimientoscaja, JsonObject.class);
		return jsonMovientosCaja;
	}

	@Override
	public JsonObject save(String movimientoscaja) {
		log.info("Guardado \n"+movimientoscaja.toString());
		JsonObject jsonMovimientosCaja = new Gson().fromJson(movimientoscaja, JsonObject.class);
		
		jsonMovimientosCaja.addProperty("movimientoid", jdbcTemplate.queryForObject("SELECT nextval('movimientoscaja_movimientoid_seq') as id;", Long.class));
//		jsonMovimientosCaja.addProperty("tipomovimientoid", "ESTU");
		jsonMovimientosCaja.addProperty("movimientofecha", Utils.getFechaActual());
		jsonMovimientosCaja.addProperty("bitacoraid", -1);
		Object[] parametros = {
				jsonMovimientosCaja.get("movimientoid").getAsInt(), jsonMovimientosCaja.get("tipomovimientoid").getAsString(), 
				jsonMovimientosCaja.get("formapagoid").getAsString(), jsonMovimientosCaja.get("usuarioid").getAsInt(), 
				jsonMovimientosCaja.get("corteid").getAsInt(), jsonMovimientosCaja.get("movimientocargo").getAsDouble(), 
				jsonMovimientosCaja.get("movimientoabono").getAsDouble(), jsonMovimientosCaja.get("movimientosaldo").getAsDouble(), 
				jsonMovimientosCaja.get("movimientocomentarios").getAsString(), jsonMovimientosCaja.get("movimientofecha").getAsString(),
				jsonMovimientosCaja.get("bitacoraid").getAsInt()};
		jdbcTemplate.update("INSERT INTO movimientoscaja(movimientoid,tipomovimientoid,formapagoid,usuarioid,corteid,"
				+"movimientocargo,movimientoabono,movimientosaldo,movimientocomentarios,movimientofecha,bitacoraid) "
				+"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, cast(? as timestamp), ?)", parametros);
		return jsonMovimientosCaja;
	}

	@Override
	public int deleteById(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}
}

package com.api.ApiLabOnline.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.api.ApiLabOnline.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Repository
public class ComprasRepositoryImpl implements ComprasRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public ComprasRepositoryImpl(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate=jdbcTemplate;
	}

	@Override
	public JsonArray getAll() {
	
		System.out.println("Buscar todos");
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from compras where compraactivo is true order by 1 desc", 
				new JsonObjectRowMapper());
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonArray list(int limit, int offset) {
		System.out.println("Buscar todos limit["+limit+"] offset["+offset+"]");
		JsonArray lista = new JsonArray();
		Object[] parameters = {limit, offset};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from compras where compraactivo is true order by 1 desc limit ? offset ?", new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonObject findById(Long id) {
	
		System.out.println("Buscar id-"+id);
		Object[] parametros = {id};
	    return jdbcTemplate.query("select * FROM compras WHERE compraid=?", new JsonObjectRowMapper(), parametros).get(0);
	}

	@Override
	public void update(String compras) {
		System.out.println("Actualizando \n"+compras.toString());
		JsonObject jsonCompras = new Gson().fromJson(compras, JsonObject.class);
		jsonCompras.addProperty("comprafechamodificacion", Utils.getFechaActual());
		Object[] parametros = {
				jsonCompras.get("compraactivo").getAsBoolean(), jsonCompras.get("compranumeroarticulos").getAsString(), 
				jsonCompras.get("compraimporteneto").getAsString(), jsonCompras.get("compraimporteiva").getAsString(), 
				jsonCompras.get("compraimportetotal").getAsNumber(), jsonCompras.get("comprafechamodificacion").getAsString(), 				
				jsonCompras.get("compraid").getAsInt()};
		jdbcTemplate.update("update compras set compraactivo=?, compranumeroarticulos=?, compraimporteneto=?, "
				+"compraimporteiva=?, compraimportetotal=?, comprafechamodificacion=cast(? as timestamp) where compraid=?", parametros);
	}

	@Override
	public void save(String compras) {
		System.out.println("Guardado \n"+compras.toString());
		JsonObject jsonCompras = new Gson().fromJson(compras, JsonObject.class);
		
		jsonCompras.addProperty("compraid", jdbcTemplate.queryForObject("SELECT nextval('compras_compraid_seq') as id;", Long.class));
		jsonCompras.addProperty("compraactivo", true);
		jsonCompras.addProperty("comprafechacreacion", Utils.getFechaActual());
		jsonCompras.addProperty("comprafechamodificacion", Utils.getFechaActual());
		jsonCompras.addProperty("bitacoraid", -1);
		Object[] parametros = {
				jsonCompras.get("compraid").getAsString(), jsonCompras.get("proveedorid").getAsString(), 
				jsonCompras.get("compraactivo").getAsString(), jsonCompras.get("compranumeroarticulos").getAsString(), 
				jsonCompras.get("compraimporteneto").getAsString(), jsonCompras.get("compraimporteiva").getAsString(), 
				jsonCompras.get("compraimportetotal").getAsString(), jsonCompras.get("comprafechacreacion").getAsString(), 
				jsonCompras.get("comprafechamodificacion").getAsString(), jsonCompras.get("bitacoraid").getAsString()};
		jdbcTemplate.update("INSERT INTO compras(compraid,proveedorid,compraactivo,compranumeroarticulos,compraimporteneto,"
				+"compraimporteiva,compraimportetotal,comprafechacreacion,comprafechamodificacion,bitacoraid) "
				+"VALUES(?, ?, ?, ?, ?, ?, ?, cast(? as timestamp), cast(? as timestamp), ?)", parametros);
	}

	@Override
	public int deleteById(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}
}

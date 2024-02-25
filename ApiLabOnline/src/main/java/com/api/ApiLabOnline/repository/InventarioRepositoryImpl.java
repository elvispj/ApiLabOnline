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
public class InventarioRepositoryImpl implements InventarioRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public InventarioRepositoryImpl(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate=jdbcTemplate;
	}

	@Override
	public JsonArray getAll() {
		System.out.println("Buscar todos");
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from inventario where inventarioactivo is true order by 1 desc", 
				new JsonObjectRowMapper());
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonArray getAllTipoProducto() {
		System.out.println("Buscar por tipo producto");
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query(
				"select i.*, p.tipoproductonombre, pr.proveedornombre "
				+ "from inventario i "
				+ "join tipoproducto p using(tipoproductoid) "
				+ "join compras c using(compraid) "
				+ "join proveedores pr using(proveedorid) "
				+ "where i.inventarioactivo is true "
				+ "order by 1 desc", 
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
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from inventario where inventarioactivo is true order by 1 desc limit ? offset ?", new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonObject findById(Long id) {
		System.out.println("Buscar id-"+id);
		Object[] parametros = {id};
	    return jdbcTemplate.query("select * FROM inventario WHERE inventarioid=?", new JsonObjectRowMapper(), parametros).get(0);
	}

	@Override
	public void update(String inventario) {
		System.out.println("Actualizando \n"+inventario.toString());
		JsonObject jsonInventario = new Gson().fromJson(inventario, JsonObject.class);
		jsonInventario.addProperty("inventariofechamodificacion", Utils.getFechaActual());
		Object[] parametros = {
				jsonInventario.get("inventarioactivo").getAsBoolean(), jsonInventario.get("inventariounidad").getAsString(), 
				jsonInventario.get("inventariocostoporunidad").getAsString(), jsonInventario.get("inventariocantidadoriginal").getAsString(), 
				jsonInventario.get("inventariocantidadactual").getAsNumber(), jsonInventario.get("inventarioimagen").getAsString(), 				
				jsonInventario.get("inventariofechamodificacion").getAsString(), jsonInventario.get("inventarioid").getAsInt()};
		jdbcTemplate.update("update inventario set inventarioactivo=?, inventariounidad=?, inventariocostoporunidad=?, "
				+"inventariocantidadoriginal=?, inventariocantidadactual=?, inventarioimagen=?, "
				+"inventariofechamodificacion=cast(? as timestamp) where inventarioid=?", parametros);
	}

	@Override
	public void save(String inventario) {
		System.out.println("Guardado \n"+inventario.toString());
		JsonObject jsonInventario = new Gson().fromJson(inventario, JsonObject.class);
		
		jsonInventario.addProperty("inventarioid", jdbcTemplate.queryForObject("SELECT nextval('inventario_inventarioid_seq') as id;", Long.class));
		jsonInventario.addProperty("inventarioactivo", true);
		jsonInventario.addProperty("inventariofechacreacion", Utils.getFechaActual());
		jsonInventario.addProperty("inventariofechamodificacion", Utils.getFechaActual());
		jsonInventario.addProperty("bitacoraid", -1);
		Object[] parametros = {
				jsonInventario.get("inventarioid").getAsString(), jsonInventario.get("compraid").getAsString(), 
				jsonInventario.get("tipoproductoid").getAsString(), jsonInventario.get("inventarioactivo").getAsString(), 
				jsonInventario.get("inventariounidad").getAsString(), jsonInventario.get("inventariocostoporunidad").getAsString(), 
				jsonInventario.get("inventariocantidadoriginal").getAsString(), jsonInventario.get("inventariocantidadactual").getAsString(), 
				jsonInventario.get("inventarioimagen").getAsString(), jsonInventario.get("inventariofechacreacion").getAsString(),
				jsonInventario.get("inventariofechamodificacion").getAsString()};
		jdbcTemplate.update("INSERT INTO inventario(inventarioid,compraid,tipoproductoid,inventarioactivo,inventariounidad,inventariocostoporunidad,"
				+"inventariocantidadoriginal,inventariocantidadactual,inventarioimagen,inventariofechacreacion,inventariofechamodificacion) "
				+"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, cast(? as timestamp), cast(? as timestamp))", parametros);
	}

	@Override
	public int deleteById(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}
}

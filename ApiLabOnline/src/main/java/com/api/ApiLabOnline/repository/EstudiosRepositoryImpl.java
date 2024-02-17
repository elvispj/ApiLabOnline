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
public class EstudiosRepositoryImpl implements EstudiosRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    public EstudiosRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

	@Override
	public JsonArray getAll() {
		System.out.println("Buscar todos");
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from estudios where estudioactivo is true order by 1 desc", 
				new JsonObjectRowMapper());
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonArray listEstudios(int limit, int offset) {
		System.out.println("Buscar todos limit["+limit+"] offset["+offset+"]");
		JsonArray lista = new JsonArray();
		Object[] parameters = {limit, offset};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from estudios where estudioactivo is true order by 1 desc limit ? offset ?", new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonObject findById(Long id) {
		System.out.println("Buscar id-"+id);
		Object[] parametros = {id};
	    return jdbcTemplate.query("select * FROM estudios WHERE estudioid=?", new JsonObjectRowMapper(), parametros).get(0);
	}

	@Override
	public void save(String estudio) {
		System.out.println("Guardado \n"+estudio.toString());
		JsonObject jsonEstudio = new Gson().fromJson(estudio, JsonObject.class);
		
		jsonEstudio.addProperty("estudioid", jdbcTemplate.queryForObject("SELECT nextval('estudios_estudioid_seq') as id;", Long.class));
		Object[] parametros = {
				jsonEstudio.get("estudioid").getAsString(), jsonEstudio.get("tipoestudioid").getAsString(), 
				jsonEstudio.get("estudioactivo").getAsString(), jsonEstudio.get("estudionombre").getAsString(), 
				jsonEstudio.get("estudiodescripcion").getAsString(), jsonEstudio.get("estudiofechacreacion").getAsString(), 
				jsonEstudio.get("estudiofechamodificacion").getAsString(), jsonEstudio.get("bitacoraid").getAsString(), 
				jsonEstudio.get("estudionombrecorto").getAsString(), jsonEstudio.get("estudiocosto").getAsString()};
		jdbcTemplate.update("INSERT INTO estudios("
				+ "estudioid, tipoestudioid, estudioactivo, estudionombre, estudiodescripcion, estudiofechacreacion, estudiofechamodificacion, bitacoraid, estudionombrecorto, estudiocosto) "
				+"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, )", parametros);
	}

	@Override
	public void update(String estudio) {
		System.out.println("Actualizando \n"+estudio.toString());
		JsonObject jsonEstudio = new Gson().fromJson(estudio, JsonObject.class);
		jsonEstudio.addProperty("estudiofechamodificacion", Utils.getFechaActual());
		Object[] parametros = {
				jsonEstudio.get("estudioactivo").getAsBoolean(), jsonEstudio.get("estudionombre").getAsString(), 
				jsonEstudio.get("estudiodescripcion").getAsString(), jsonEstudio.get("estudionombrecorto").getAsString(), 
				jsonEstudio.get("estudiocosto").getAsNumber(), jsonEstudio.get("estudiofechamodificacion").getAsString(), 				
				jsonEstudio.get("estudioid").getAsInt()};
		jdbcTemplate.update("update estudios set estudioactivo=?, estudionombre=?, estudiodescripcion=?, "
				+"estudionombrecorto=?, estudiocosto=?, estudiofechamodificacion=cast(? as timestamp) where estudioid=?", parametros);
	}

	@Override
	public int deleteById(Long id) {
		System.out.println("Eliminado id-"+id);
		Object[] parametros = {id};
	    return jdbcTemplate.update("DELETE FROM estudios WHERE estudioid=? and 1=2", parametros);
	}
	
}

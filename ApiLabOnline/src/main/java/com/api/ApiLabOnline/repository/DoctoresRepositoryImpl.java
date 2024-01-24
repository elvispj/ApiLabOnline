package com.api.ApiLabOnline.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Repository
public class DoctoresRepositoryImpl implements DoctoresRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public DoctoresRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

	@Override
	public JsonArray all() {
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from doctores where doctoractivo is true order by doctornombre ", new JsonObjectRowMapper());
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonArray list(int limit, int offset) {
		JsonArray lista = new JsonArray();
		Object[] parameters = {limit, offset};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from doctores order by 1 desc limit ? offset ?", new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonObject findById(Long doctorid) {
		JsonObject jsonObject = new JsonObject();
		List<JsonObject> e = jdbcTemplate.query("select * FROM doctores WHERE doctorid=?",new JsonObjectRowMapper(), doctorid);
		if(e!=null && e.size()>0) {
			jsonObject = e.get(0).getAsJsonObject();
			System.out.println(jsonObject.toString());
		}
		return jsonObject;
	}

	@Override
	public JsonObject save(String jsonDoctor) {
		JsonObject doctor = new Gson().fromJson(jsonDoctor, JsonObject.class);
		
		doctor.addProperty("doctorid", jdbcTemplate.queryForObject("SELECT nextval('doctores_doctorid_seq') as id;", Long.class));
		
		System.out.println(doctor.toString());

		Object[] parametros = {doctor.get("tipoestudioid").getAsLong(), doctor.get("tipoestudioactivo").getAsBoolean(), doctor.get("tipoestudionombre").getAsString(), 
				doctor.get("tipoestudiodescripcion").getAsString(), doctor.get("tipoestudiofechacreacion").getAsString(), 
				doctor.get("tipoestudiofechamodificacion").getAsString(), doctor.get("bitacoraid").getAsInt()};
		jdbcTemplate.update("INSERT INTO doctores(doctorid, doctoractivo, doctornombre, doctorapellidopaterno, doctorapellidomaterno, "
				+ "doctorcedula, doctortitulo, doctorfechacreacion, doctorfechamodificacion, bitacoraid) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, cast(? as timestamp), cast(? as timestamp), ?);", parametros);
		System.out.println("Se registro exitosamente "+doctor.get("tipoestudioid").getAsLong());
		return doctor;
	}

}

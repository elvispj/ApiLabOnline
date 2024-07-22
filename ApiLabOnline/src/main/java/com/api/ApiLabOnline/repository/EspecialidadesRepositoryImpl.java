package com.api.ApiLabOnline.repository;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Repository
public class EspecialidadesRepositoryImpl implements EspecialidadesRepository {
	private Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public EspecialidadesRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public JsonArray listByDoctorid(Long doctorid) {
		log.info("Buscar ByDoctorid-"+doctorid);
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select e.* from especialidades e join especialidaddoctor d using(especialidadid)"
				+" where especialidaddoctoractivo is true and doctorid=? order by 1", 
				new JsonObjectRowMapper(), doctorid);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

}

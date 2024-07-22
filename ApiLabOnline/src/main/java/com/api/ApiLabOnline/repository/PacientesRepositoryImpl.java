package com.api.ApiLabOnline.repository;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Repository
public class PacientesRepositoryImpl implements PacientesRepository {
	private Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public PacientesRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public JsonArray getAll() {
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from pacientes where pacienteactivo is true order by 1 desc ", 
				new JsonObjectRowMapper());
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonArray list(int limit, int offset) {
		JsonArray lista = new JsonArray();
		Object[] parameters = {limit, offset};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from pacientes where pacienteactivo is true order by 1 desc limit ? offset ?", 
				new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonArray listByDoctorid(Long doctorid) {
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from pacientes where pacienteactivo is true "
				+" and doctorid=? order by 1 desc", 
				new JsonObjectRowMapper(), doctorid);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonObject findById(Long pacienteid) {
		try {
			return jdbcTemplate.queryForObject("select * FROM pacientes WHERE pacienteactivo is true and pacienteid=?",
					new JsonObjectRowMapper(), pacienteid);
	    } catch (EmptyResultDataAccessException e) {
	    	log.info("NO encontro informacion del paciente");
	        return null;
	    }
	}

	@Override
	public JsonObject save(String jsonPaciente) {
		// TODO Auto-generated method stub
		return null;
	}

}

package com.api.ApiLabOnline.repository;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Repository
public class FormasPagoRepositoryImpl implements FormasPagoRepository {
	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public FormasPagoRepositoryImpl(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate=jdbcTemplate;
	}

	@Override
	public JsonArray getAll() {
		log.debug("Buscar todos");
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from formaspago where formapagoactivo is true order by 1 desc", 
				new JsonObjectRowMapper());
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonObject getById(String formapagoid) {
		log.debug("Buscar id-"+formapagoid);
		try {
			return jdbcTemplate.queryForObject("select * FROM formaspago WHERE formapagoid=?", new JsonObjectRowMapper(), formapagoid);
	    } catch (EmptyResultDataAccessException e) {
	    	log.info("NO encontro informacion");
	        return null;
	    }
	}

	@Override
	public JsonObject saveOrUpdate(String jsonTipoestudio) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long tipoproductoid) {
		// TODO Auto-generated method stub
	}

}

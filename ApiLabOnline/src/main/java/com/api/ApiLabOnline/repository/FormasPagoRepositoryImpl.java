package com.api.ApiLabOnline.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Repository
public class FormasPagoRepositoryImpl implements FormasPagoRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public FormasPagoRepositoryImpl(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate=jdbcTemplate;
	}

	@Override
	public JsonArray getAll() {
		System.out.println("Buscar todos");
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
		System.out.println("Buscar id-"+formapagoid);
		Object[] parametros = {formapagoid};
	    return jdbcTemplate.query("select * FROM formaspago WHERE formapagoid=?", new JsonObjectRowMapper(), parametros).get(0);
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

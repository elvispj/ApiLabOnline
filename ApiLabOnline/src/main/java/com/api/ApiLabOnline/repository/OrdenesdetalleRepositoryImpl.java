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
public class OrdenesdetalleRepositoryImpl implements OrdenesdetalleRepository {
	private Logger log = LogManager.getLogger(OrdenesdetalleRepositoryImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    public OrdenesdetalleRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

	@Override
	public JsonArray all() {
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from ordendetalle where ordendetalleactivo is true order by 1 ", new JsonObjectRowMapper());
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonArray list(int limit, int offset) {
		JsonArray lista = new JsonArray();
		Object[] parameters = {limit, offset};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from ordendetalle order by 1 desc limit ? offset ?", new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonObject findById(Long ordendetalleid) {
		JsonObject jsonObject = new JsonObject();
		List<JsonObject> e = jdbcTemplate.query("select * FROM ordendetalle WHERE ordendetalleid=?",new JsonObjectRowMapper(), ordendetalleid);
		if(e!=null && e.size()>0) {
			jsonObject = e.get(0).getAsJsonObject();
		}
		return jsonObject;
	}

	@Override
	public JsonObject save(String jsonOrdendetalleid) {
		JsonObject ordendetalle = new Gson().fromJson(jsonOrdendetalleid, JsonObject.class);
		
		ordendetalle.addProperty("ordendetalleid", jdbcTemplate.queryForObject("SELECT nextval('ordendetalle_ordendetalleid_seq') as id;", Long.class));
		ordendetalle.addProperty("ordendetallefechacreacion", Utils.getFechaActual());
		ordendetalle.addProperty("ordendetallefechamodificacion", Utils.getFechaActual());
		ordendetalle.addProperty("bitacoraid", -1);
		

		Object[] parametros = {ordendetalle.get("ordendetalleid").getAsLong(), ordendetalle.get("ordenid").getAsLong(), ordendetalle.get("estudioid").getAsLong(), 
				ordendetalle.get("ordendetalleactivo").getAsBoolean(), ordendetalle.get("ordendetallecosto").getAsDouble(), 
				ordendetalle.get("ordendetalledescuento").getAsDouble(), ordendetalle.get("ordendetalleimportedescuento").getAsDouble(),
				ordendetalle.get("ordendetallecostofinal").getAsDouble(), ordendetalle.get("ordendetallefechacreacion").getAsString(), 
				ordendetalle.get("ordendetallefechamodificacion").getAsString(), ordendetalle.get("bitacoraid").getAsInt()};
		jdbcTemplate.update("insert into ordendetalle(ordendetalleid,ordenid,estudioid,ordendetalleactivo,ordendetallecosto,"
				+"ordendetalledescuento,ordendetalleimportedescuento,ordendetallecostofinal,ordendetallefechacreacion,ordendetallefechamodificacion,bitacoraid) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, cast(? as timestamp), cast(? as timestamp), ?);", parametros);
		log.info("Se registro exitosamente "+ordendetalle.get("ordendetalleid").getAsLong());
		return ordendetalle;
	}

}

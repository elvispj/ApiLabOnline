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
public class ClientesRepositoryImpl implements ClientesRepository {
	private Logger log = LogManager.getLogger(ClientesRepositoryImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public ClientesRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

	@Override
	public JsonArray all() {
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from clientes where clienteactivo is true order by clientenombre ", new JsonObjectRowMapper());
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonArray list(int limit, int offset) {
		JsonArray lista = new JsonArray();
		Object[] parameters = {limit, offset};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from clientes order by 1 desc limit ? offset ?", new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonObject findById(Long clienteid) {
		JsonObject jsonObject = new JsonObject();
		List<JsonObject> e = jdbcTemplate.query("select * FROM clientes WHERE clienteid=?",new JsonObjectRowMapper(), clienteid);
		if(e!=null && e.size()>0) {
			jsonObject = e.get(0).getAsJsonObject();
			System.out.println(jsonObject.toString());
		}
		return jsonObject;
	}

	@Override
	public JsonArray findByLike(String parametro) {
		JsonArray lista = new JsonArray();
		Object[] parameters = {"%"+parametro.toUpperCase()+"%"};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * FROM clientes "
				+"WHERE upper(trim(clientenombre)||' '||trim(clienteapellidopaterno)||' '||trim(clienteapellidomaterno)) like ? "
				+"order by clientenombre",new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonObject update(String jsoncliente) {
		JsonObject cliente = new Gson().fromJson(jsoncliente, JsonObject.class);

		cliente.addProperty("clientefechamodificacion", Utils.getFechaActual());

		Object[] parametros = {cliente.get("clienteactivo").getAsBoolean(), cliente.get("clientenombre").getAsString(), cliente.get("clienteapellidopaterno").getAsString(), 
				cliente.get("clienteapellidomaterno").getAsString(), cliente.get("clienteedad").getAsString(), cliente.get("clientesexo").getAsString(), 
				cliente.get("clientetelefono").getAsString(), cliente.get("clientedireccion").getAsString(), cliente.get("clientedatosclinicos").getAsString(), 
				cliente.get("clientefechamodificacion").getAsString(), cliente.get("clienteid").getAsInt()};
		jdbcTemplate.update("update clientes set clienteactivo=?, clientenombre=?, clienteapellidopaterno=?, clienteapellidomaterno=?, "
				+ "clienteedad=?, clientesexo=?, clientetelefono=?, clientedireccion=?, clientedatosclinicos=? "
				+ "clientefechamodificacion=cast(? as timestamp) where clienteid=? ", parametros);
		
		log.info("Se actualizo "+cliente.toString());
		return cliente;
	}

	@Override
	public JsonObject save(String jsoncliente) {
		JsonObject cliente = new Gson().fromJson(jsoncliente, JsonObject.class);
		
		cliente.addProperty("clienteid", jdbcTemplate.queryForObject("SELECT nextval('clientes_clienteid_seq') as id;", Long.class));
		cliente.addProperty("clienteactivo", true);
		cliente.addProperty("clientefechacreacion", Utils.getFechaActual());
		cliente.addProperty("clientefechamodificacion", Utils.getFechaActual());
		cliente.addProperty("bitacoraid", -1);

		Object[] parametros = {cliente.get("clienteid").getAsLong(), cliente.get("clienteactivo").getAsBoolean(), 
				cliente.get("clientetipo").getAsString(), cliente.get("clientenombre").getAsString(), 
				cliente.get("clienteapellidopaterno").getAsString(), cliente.get("clienteapellidomaterno").getAsString(), 
				cliente.get("clientefechacreacion").getAsString(), cliente.get("clientefechamodificacion").getAsString(),
				cliente.get("bitacoraid").getAsInt(), cliente.get("clienteedad").getAsInt(), cliente.get("clientesexo").getAsString(),
				cliente.get("clientetelefono").getAsString(), cliente.get("clientedireccion").getAsString(), cliente.get("clientedatosclinicos").getAsString()};
		jdbcTemplate.update("INSERT INTO clientes(clienteid, clienteactivo, clientetipo, clientenombre, clienteapellidopaterno, "
				+"clienteapellidomaterno, clientefechacreacion, clientefechamodificacion, bitacoraid, clienteedad, clientesexo, "
				+"clientetelefono, clientedireccion, clientedatosclinicos) "
				+ "VALUES(?, ?, ?, ?, ?, ?, cast(? as timestamp), cast(? as timestamp), ?, ?, ?, ?, ?, ?);", parametros);
		log.info("Se registro exitosamente "+cliente.toString());
		return cliente;
	}

}

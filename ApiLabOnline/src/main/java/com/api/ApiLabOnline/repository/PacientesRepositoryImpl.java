package com.api.ApiLabOnline.repository;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.api.ApiLabOnline.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
		JsonObject paciente = new Gson().fromJson(jsonPaciente, JsonObject.class);
		
		paciente.addProperty("pacienteid", jdbcTemplate.queryForObject("SELECT nextval('pacientes_pacienteid_seq') as id;", Long.class));
		paciente.addProperty("pacienteactivo", true);
		paciente.addProperty("pacientefechacreacion", Utils.getFechaActual());
		paciente.addProperty("pacientefechamodificacion", Utils.getFechaActual());
		paciente.addProperty("bitacoraid", -1);

		Object[] parametros = {paciente.get("pacienteid").getAsLong(), paciente.get("pacienteactivo").getAsBoolean(), paciente.get("doctorid").getAsInt(), 
				paciente.get("pacientenombre").getAsString(), paciente.get("pacienteapellidopaterno").getAsString(), paciente.get("pacienteapellidomaterno").getAsString(), 
				paciente.get("pacientesexo").getAsString(), paciente.get("pacientefechanacimiento").getAsString(), paciente.get("pacienteedad").getAsInt(), 
				paciente.get("pacientetiposangre").getAsString(), paciente.get("pacienteemail").getAsString(), paciente.get("pacientetelefono").getAsString(), 
				paciente.get("pacientedireccion").getAsString(), paciente.get("pacientefechacreacion").getAsString(), 
				paciente.get("pacientefechamodificacion").getAsString(), paciente.get("bitacoraid").getAsInt()};
		log.info(paciente.toString());
		jdbcTemplate.update("INSERT INTO pacientes(pacienteid, pacienteactivo, doctorid, pacientenombre, pacienteapellidopaterno, pacienteapellidomaterno, "
				+"pacientesexo, pacientefechanacimiento, pacienteedad, pacientetiposangre, pacienteemail, pacientetelefono, pacientedireccion, pacientefechacreacion, pacientefechamodificacion, bitacoraid) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?::date, ?, ?, ?, ?, ?, ?::timestamp, ?::timestamp, ?);", parametros);
		log.info("Se registro exitosamente el paciente "+paciente.get("pacienteid").getAsLong());
		return paciente;
	}

	@Override
	public JsonElement update(String jsonPaciente) {
		JsonObject paciente = new Gson().fromJson(jsonPaciente, JsonObject.class);
		paciente.addProperty("pacientefechamodificacion", Utils.getFechaActual());

		Object[] parametros = {paciente.get("pacienteactivo").getAsBoolean(), paciente.get("doctorid").getAsInt(), paciente.get("pacientenombre").getAsString(), 
				paciente.get("pacienteapellidopaterno").getAsString(), paciente.get("pacienteapellidomaterno").getAsString(), paciente.get("pacientesexo").getAsString(), 
				paciente.get("pacientefechanacimiento").getAsString(), paciente.get("pacienteedad").getAsInt(), paciente.get("pacientetiposangre").getAsString(), 
				paciente.get("pacienteemail").getAsString(), paciente.get("pacientetelefono").getAsString(), paciente.get("pacientedireccion").getAsString(), 
				paciente.get("pacientefechamodificacion").getAsString(), paciente.get("pacienteid").getAsLong()};
		int count = jdbcTemplate.update("UPDATE pacientes SET pacienteactivo=?, doctorid=?, pacientenombre=?, pacienteapellidopaterno=?, pacienteapellidomaterno=?, "
				+"pacientesexo=?, pacientefechanacimiento=?::date, pacienteedad=?, pacientetiposangre=?, pacienteemail=?, pacientetelefono=?, pacientedireccion=?, "
				+"pacientefechamodificacion=?::timestamp WHERE pacienteid=?;", parametros);
		if(count<1) {
			log.info("No se logro actualizar la informacion del paciente");
			return null;
		}
		log.info("Se actualizo exitosamente el pacienteid "+paciente.get("pacienteid").getAsLong());
		return paciente;
	}

}

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
public class OrdenesRepositoryImpl implements OrdenesRepository {
	private Logger log = LogManager.getLogger(OrdenesRepositoryImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    public OrdenesRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

	@Override
	public JsonArray listOrdenes(int limit, int offset) {
		JsonArray lista = new JsonArray();
		Object[] parameters = {limit, offset};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from ordenes where ordenactiva is true order by ordenfechacreacion limit ? offset ?", new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonObject findById(Long id) {
		JsonObject jsonObject = new JsonObject();
		JsonArray ordendetalle = new JsonArray();
		List<JsonObject> listResponse = jdbcTemplate.query("select * FROM ordenes WHERE ordenid=?",new JsonObjectRowMapper(),id);
		if(listResponse==null || listResponse.size()<1) {
			return jsonObject;
		}
		jsonObject = listResponse.get(0).getAsJsonObject();
		
		listResponse = jdbcTemplate.query("select * FROM doctores WHERE doctorid=?",new JsonObjectRowMapper(),jsonObject.get("doctorid").getAsInt());
		if(listResponse!=null && listResponse.size()>0) {
			jsonObject.add("doctor", listResponse.get(0).getAsJsonObject());
		}
		
		listResponse = jdbcTemplate.query("select * FROM clientes WHERE clienteid=?",new JsonObjectRowMapper(),jsonObject.get("clienteid").getAsInt());
		if(listResponse!=null && listResponse.size()>0) {
			jsonObject.add("cliente", listResponse.get(0).getAsJsonObject());
		}

		listResponse = jdbcTemplate.query("select o.*,e.estudionombre from ordendetalle o join estudios e using(estudioid) where ordenid=? order by 1", new JsonObjectRowMapper(), jsonObject.get("ordenid").getAsInt());
		for(JsonObject jsonObjecto: listResponse) {
			ordendetalle.add(jsonObjecto);
		}
		jsonObject.add("ordenesdetalle", ordendetalle);
		return jsonObject;
	}

	@Override
	public JsonObject update(String jsonOrden) {
		JsonObject orden = new Gson().fromJson(jsonOrden, JsonObject.class);
		
		orden.addProperty("ordenfechamodificacion", Utils.getFechaActual());

		Object[] parametros = {orden.get("ordenactiva").getAsBoolean(), orden.get("colaboradorid").getAsInt(), 
				orden.get("clienteid").getAsInt(), orden.get("ordennombre").getAsString(), orden.get("ordenedad").getAsInt(), 
				orden.get("ordentelefono").getAsString(), orden.get("ordendireccion").getAsString(), orden.get("ordenformaentrega").getAsString(), 
				orden.get("ordenfechamodificacion").getAsString(), orden.get("doctorid").getAsInt(), 
				orden.get("ordenorigen").getAsString(), orden.get("ordencomentarios").getAsString(), 
				orden.get("ordenimporte").getAsDouble(), orden.get("ordenimporteiva").getAsDouble(), orden.get("ordendescuento").getAsDouble(), 
				orden.get("ordenimportedescuento").getAsDouble(), orden.get("ordenimportetotal").getAsDouble(), orden.get("ordencomoubico").getAsString(), 
				orden.get("ordendatosclinicos").getAsString(), orden.get("ordenimportemaquila").getAsDouble(), orden.get("ordensexo").getAsString(), 
				orden.get("formapagoid").getAsString(), orden.get("ordenimporteotrocobro").getAsDouble(), orden.get("ordenid").getAsLong()};
		jdbcTemplate.update("UPDATE ordenes "
				+ "SET ordenactiva=?,colaboradorid=?, clienteid=?, ordennombre=?, ordenedad=?, ordentelefono=?, ordendireccion=?, ordenformaentrega=?, "
				+ "ordenfechamodificacion=?::timestamp, doctorid=?, ordenorigen=?, ordencomentarios=?, ordenimporte=?, ordenimporteiva=?, "
				+ "ordendescuento=?, ordenimportedescuento=?, ordenimportetotal=?, ordencomoubico=?, ordendatosclinicos=?, ordenimportemaquila=?, ordensexo=?, "
				+ "formapagoid=?, ordenimporteotrocobro=? WHERE ordenid=?;", parametros);
		log.info("Se actualizo exitosamente "+orden.get("ordenid").getAsLong());
		return orden;
	}

	@Override
	public JsonObject save(String jsonOrden) {
		JsonObject orden = new Gson().fromJson(jsonOrden, JsonObject.class);
		
		orden.addProperty("ordenid", jdbcTemplate.queryForObject("SELECT nextval('ordenes_orderid_seq') as id;", Long.class));
		orden.addProperty("ordenactiva", true);
		orden.addProperty("ordenfechacreacion", Utils.getFechaActual());
		orden.addProperty("ordenfechamodificacion", Utils.getFechaActual());
		orden.addProperty("bitacoraid", -1);

		Object[] parametros = {orden.get("ordenid").getAsLong(), orden.get("ordenactiva").getAsBoolean(), orden.get("colaboradorid").getAsInt(), 
				orden.get("clienteid").getAsInt(), orden.get("ordennombre").getAsString(), orden.get("ordenedad").getAsInt(), 
				orden.get("ordentelefono").getAsString(), orden.get("ordendireccion").getAsString(), orden.get("ordenformaentrega").getAsString(), 
				orden.get("ordenfechacreacion").getAsString(), orden.get("ordenfechamodificacion").getAsString(), orden.get("doctorid").getAsInt(), 
				orden.get("bitacoraid").getAsInt(), orden.get("ordenorigen").getAsString(), orden.get("ordencomentarios").getAsString(), 
				orden.get("ordenimporte").getAsDouble(), orden.get("ordenimporteiva").getAsDouble(), orden.get("ordendescuento").getAsDouble(), 
				orden.get("ordenimportedescuento").getAsDouble(), orden.get("ordenimportetotal").getAsDouble(), orden.get("ordencomoubico").getAsString(), 
				orden.get("ordendatosclinicos").getAsString(), orden.get("ordenimportemaquila").getAsDouble(), orden.get("ordensexo").getAsString(), 
				orden.get("formapagoid").getAsString(), orden.get("ordenimporteotrocobro").getAsDouble()};
		jdbcTemplate.update("INSERT INTO ordenes(ordenid, ordenactiva, colaboradorid, clienteid, ordennombre, "
				+"ordenedad, ordentelefono, ordendireccion, ordenformaentrega, ordenfechacreacion, ordenfechamodificacion, "
				+"doctorid, bitacoraid, ordenorigen, ordencomentarios, ordenimporte, ordenimporteiva, ordendescuento, "
				+"ordenimportedescuento, ordenimportetotal, ordencomoubico, ordendatosclinicos, ordenimportemaquila, "
				+"ordensexo, formapagoid, ordenimporteotrocobro) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, cast(? as timestamp), cast(? as timestamp), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", parametros);
		log.info("Se registro exitosamente "+orden.get("ordenid").getAsLong());
		return orden;
	}

	@Override
	public int deleteById(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}
//	******************************************************
//	public List<Estudios> findAll() {
//		log.info("Buscar todos");
////		return jdbcTemplate.query("SELECT * from tbl_student", BeanPropertyRowMapper.newInstance(Student.class));
//		return jdbcTemplate.query("SELECT * from tbl_student", (rs, rowNum) -> new Estudios(rs.getLong("student_id"),
//				rs.getString("first_name"), rs.getString("last_name"), rs.getString("email_address")));
//	}
//
////	public Optional<Estudios> findById(Long id) {
////		System.out.println("Buscar id-"+id);
////		Object[] parametros = {id};
//////	    return jdbcTemplate.queryForObject("select * FROM tbl_student WHERE student_id=?", 
//////	    		BeanPropertyRowMapper.newInstance(Student.class), id);
//////	    return jdbcTemplate.query("select * FROM tbl_student WHERE student_id=?", 
//////	    		BeanPropertyRowMapper.newInstance(Student.class), parametros).stream().findFirst();
////	    return jdbcTemplate.query("select * FROM tbl_student WHERE student_id=?", 
////	    		(rs, rowNum) -> new Estudios(rs.getLong("student_id"),
////	    				rs.getString("first_name"), rs.getString("last_name"), rs.getString("email_address")), parametros).stream().findFirst();
////	}
//
//	public void save(Estudios student) {
//		System.out.println("Guardado \n"+student.toString());
//		Object[] parametros = {student.getEmail_address(), student.getFirstName(), student.getLastName()};
//		jdbcTemplate.update("INSERT INTO tbl_student ( email_address, first_name, last_name) VALUES(?,?,?)", parametros);
//	}
//
//	public int deleteByIdd(Long id) {
//		System.out.println("Eliminado id-"+id);
//		Object[] parametros = {id};
//	    return jdbcTemplate.update("DELETE FROM tbl_student WHERE student_id=?", parametros);
//	}
	
	
	
//	class AnyObjectMapper implements RowMapper<Map<String, Object>> {
//
//	    public AnyObjectMapper() {
//	        // TODO Auto-generated constructor stub
//	    }
//
//	    @Override
//	    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
//
//	        ResultSetMetaData rsMeta = rs.getMetaData();
//	        int colCount = rsMeta.getColumnCount();
//
//	        Map<String, Object> columns = new HashMap<String, Object>();
//	        for (int i = 1; i <= colCount; i++) {
//
//	            columns.put(rsMeta.getColumnLabel(i), rs.getObject(i));
//	        }
//	        return columns;
//
//	    }
//	}
}

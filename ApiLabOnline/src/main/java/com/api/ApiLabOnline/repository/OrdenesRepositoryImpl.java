package com.api.ApiLabOnline.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.api.ApiLabOnline.entity.Estudios;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Repository
public class OrdenesRepositoryImpl implements OrdenesRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    public OrdenesRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

	@Override
	public JsonArray listOrdenes(int limit, int offset) {
		JsonArray lista = new JsonArray();
		Object[] parameters = {limit, offset};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from ordenes order by 1 desc limit ? offset ?", new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		System.out.println("tamanio de la lista "+lista.size());
		return lista;
	}

	@Override
	public JsonObject findById(Long id) {
		JsonObject jsonObject = new JsonObject();
		List<JsonObject> e = jdbcTemplate.query("select * FROM ordenes WHERE ordenid=?",new JsonObjectRowMapper(),id);
		if(e!=null && e.size()>0) {
			jsonObject = e.get(0).getAsJsonObject();
			System.out.println(jsonObject.toString());
		}
		return jsonObject;
	}

	@Override
	public JsonObject save(String jsonOrden) {
		JsonObject orden = new Gson().fromJson(jsonOrden, JsonObject.class);
		
		orden.addProperty("ordenid", jdbcTemplate.queryForObject("SELECT nextval('ordenes_orderid_seq') as id;", Long.class));
		
		System.out.println(orden.toString());

		Object[] parametros = {orden.get("ordenid").getAsLong(), orden.get("ordenactiva").getAsBoolean(), orden.get("colaboradorid").getAsInt(), 
				orden.get("clienteid").getAsInt(), orden.get("ordennombre").getAsString(), orden.get("ordenedad").getAsInt(), 
				orden.get("ordentelefono").getAsString(), orden.get("ordendireccion").getAsString(), orden.get("ordenformaentrega").getAsString(), 
				orden.get("ordenfechacreacion").getAsString(), orden.get("ordenfechamodificacion").getAsString(), orden.get("doctorid").getAsInt(), 
				orden.get("bitacoraid").getAsInt(), orden.get("ordenorigen").getAsString(), orden.get("ordencomentarios").getAsString(), 
				orden.get("ordenimporte").getAsDouble(), orden.get("ordenimporteiva").getAsDouble(), orden.get("ordendescuento").getAsDouble(), 
				orden.get("ordenimportedescuento").getAsDouble(), orden.get("ordenimportetotal").getAsDouble(), orden.get("ordencomoubico").getAsString(), 
				orden.get("ordendatosclinicos").getAsString()};
		jdbcTemplate.update("INSERT INTO ordenes(ordenid, ordenactiva, colaboradorid, clienteid, ordennombre, "
				+"ordenedad, ordentelefono, ordendireccion, ordenformaentrega, ordenfechacreacion, ordenfechamodificacion, "
				+"doctorid, bitacoraid, ordenorigen, ordencomentarios, ordenimporte, ordenimporteiva, ordendescuento, "
				+"ordenimportedescuento, ordenimportetotal, ordencomoubico, ordendatosclinicos) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, cast(? as timestamp), cast(? as timestamp), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", parametros);
		System.out.println("Se registro exitosamente "+orden.get("ordenid").getAsLong());
		return orden;
	}

	@Override
	public int deleteById(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}
//	******************************************************
	public List<Estudios> findAll() {
		System.out.println("Buscar todos");
//		return jdbcTemplate.query("SELECT * from tbl_student", BeanPropertyRowMapper.newInstance(Student.class));
		return jdbcTemplate.query("SELECT * from tbl_student", (rs, rowNum) -> new Estudios(rs.getLong("student_id"),
				rs.getString("first_name"), rs.getString("last_name"), rs.getString("email_address")));
	}

//	public Optional<Estudios> findById(Long id) {
//		System.out.println("Buscar id-"+id);
//		Object[] parametros = {id};
////	    return jdbcTemplate.queryForObject("select * FROM tbl_student WHERE student_id=?", 
////	    		BeanPropertyRowMapper.newInstance(Student.class), id);
////	    return jdbcTemplate.query("select * FROM tbl_student WHERE student_id=?", 
////	    		BeanPropertyRowMapper.newInstance(Student.class), parametros).stream().findFirst();
//	    return jdbcTemplate.query("select * FROM tbl_student WHERE student_id=?", 
//	    		(rs, rowNum) -> new Estudios(rs.getLong("student_id"),
//	    				rs.getString("first_name"), rs.getString("last_name"), rs.getString("email_address")), parametros).stream().findFirst();
//	}

	public void save(Estudios student) {
		System.out.println("Guardado \n"+student.toString());
		Object[] parametros = {student.getEmail_address(), student.getFirstName(), student.getLastName()};
		jdbcTemplate.update("INSERT INTO tbl_student ( email_address, first_name, last_name) VALUES(?,?,?)", parametros);
	}

	public int deleteByIdd(Long id) {
		System.out.println("Eliminado id-"+id);
		Object[] parametros = {id};
	    return jdbcTemplate.update("DELETE FROM tbl_student WHERE student_id=?", parametros);
	}
	
	
	
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

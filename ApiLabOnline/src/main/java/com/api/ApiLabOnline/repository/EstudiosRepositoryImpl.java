package com.api.ApiLabOnline.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.api.ApiLabOnline.entity.Estudios;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Repository
public class EstudiosRepositoryImpl implements EstudiosRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    public EstudiosRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

	@Override
	public JsonArray getAll() {
		System.out.println("Buscar todos");
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from estudios where estudioactivo is true order by 1 desc", 
				new JsonObjectRowMapper());
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonArray listEstudios(int limit, int offset) {
		System.out.println("Buscar todos limit["+limit+"] offset["+offset+"]");
		JsonArray lista = new JsonArray();
		Object[] parameters = {limit, offset};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from estudios where estudioactivo is true order by 1 desc limit ? offset ?", new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
//		return jdbcTemplate.query("SELECT * from tbl_student", BeanPropertyRowMapper.newInstance(Student.class));
//		return jdbcTemplate.query("SELECT * from tbl_student", (rs, rowNum) -> new Estudios(rs.getLong("student_id"),
//				rs.getString("first_name"), rs.getString("last_name"), rs.getString("email_address")));
	}

	@Override
	public Optional<Estudios> findById(Long id) {
		System.out.println("Buscar id-"+id);
		Object[] parametros = {id};
//	    return jdbcTemplate.queryForObject("select * FROM tbl_student WHERE student_id=?", 
//	    		BeanPropertyRowMapper.newInstance(Student.class), id);
//	    return jdbcTemplate.query("select * FROM tbl_student WHERE student_id=?", 
//	    		BeanPropertyRowMapper.newInstance(Student.class), parametros).stream().findFirst();
	    return jdbcTemplate.query("select * FROM tbl_student WHERE student_id=?", 
	    		(rs, rowNum) -> new Estudios(rs.getLong("student_id"),
	    				rs.getString("first_name"), rs.getString("last_name"), rs.getString("email_address")), parametros).stream().findFirst();
	}

	@Override
	public void save(Estudios student) {
		System.out.println("Guardado \n"+student.toString());
		Object[] parametros = {student.getEmail_address(), student.getFirstName(), student.getLastName()};
		jdbcTemplate.update("INSERT INTO tbl_student ( email_address, first_name, last_name) VALUES(?,?,?)", parametros);
	}

	@Override
	public int deleteById(Long id) {
		System.out.println("Eliminado id-"+id);
		Object[] parametros = {id};
	    return jdbcTemplate.update("DELETE FROM tbl_student WHERE student_id=?", parametros);
	}
	
}

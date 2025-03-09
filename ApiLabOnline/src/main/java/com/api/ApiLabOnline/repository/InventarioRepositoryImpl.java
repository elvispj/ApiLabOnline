package com.api.ApiLabOnline.repository;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.api.ApiLabOnline.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Repository
public class InventarioRepositoryImpl implements InventarioRepository {
	private Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public InventarioRepositoryImpl(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate=jdbcTemplate;
	}

	@Override
	public JsonArray getAll() {
		log.info("Buscar todos");
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from inventario where inventarioactivo is true order by 1 desc", 
				new JsonObjectRowMapper());
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonArray getAllTipoProducto() {
		log.info("Buscar por tipo producto");
		JsonArray lista = new JsonArray();
		List<JsonObject> listaJsonObject = jdbcTemplate.query(
				"select i.*, p.tipoproductonombre, pr.proveedornombre "
				+ "from inventario i "
				+ "join tipoproducto p using(tipoproductoid) "
				+ "join compras c using(compraid) "
				+ "join proveedores pr using(proveedorid) "
				+ "where i.inventarioactivo is true "
				+ "order by 1 desc", 
				new JsonObjectRowMapper());
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonArray list(int limit, int offset) {
		log.info("Buscar todos limit["+limit+"] offset["+offset+"]");
		JsonArray lista = new JsonArray();
		Object[] parameters = {limit, offset};
		List<JsonObject> listaJsonObject = jdbcTemplate.query("select * from inventario where inventarioactivo is true order by 1 desc limit ? offset ?", new JsonObjectRowMapper(), parameters);
		for(JsonObject jsonObjecto: listaJsonObject) {
			lista.add(jsonObjecto);
		}
		return lista;
	}

	@Override
	public JsonObject findById(Long id) {
		log.info("Buscar id-"+id);
		Object[] parametros = {id};
	    return jdbcTemplate.query("select * FROM inventario WHERE inventarioid=?", new JsonObjectRowMapper(), parametros).get(0);
	}

	@Override
	public JsonObject update(String inventario) {
		log.info("Actualizando \n"+inventario.toString());
		JsonObject jsonInventario = new Gson().fromJson(inventario, JsonObject.class);
		jsonInventario.addProperty("inventariofechamodificacion", Utils.getFechaActual());
		String filename="INVENTARIO_"+jsonInventario.get("inventarioid").getAsInt()
				+"_"+jsonInventario.get("inventarioimagen").getAsString()+".jpeg";
		jsonInventario.remove("inventarioimagen");
		jsonInventario.addProperty("inventarioimagen", filename);
		log.info("jsonInventario >> "+jsonInventario.toString());
		Object[] parametros = {
				jsonInventario.get("inventarioactivo").getAsBoolean(), jsonInventario.get("inventariounidad").getAsString(), 
				jsonInventario.get("inventariocostoporunidad").getAsFloat(), jsonInventario.get("inventariocantidadoriginal").getAsFloat(), 
				jsonInventario.get("inventariocantidadactual").getAsFloat(), jsonInventario.get("inventarioimagen").getAsString(), 				
				jsonInventario.get("inventariofechamodificacion").getAsString(), jsonInventario.get("inventarioid").getAsInt()};
		jdbcTemplate.update("update inventario set inventarioactivo=?, inventariounidad=?, inventariocostoporunidad=?, "
				+"inventariocantidadoriginal=?, inventariocantidadactual=?, inventarioimagen=?, "
				+"inventariofechamodificacion=cast(? as timestamp) where inventarioid=?", parametros);
		
		return jsonInventario;
	}

	@Override
	public JsonObject save(String inventario) {
		log.info("Guardado \n"+inventario.toString());
		JsonObject jsonInventario = new Gson().fromJson(inventario, JsonObject.class);
		
		jsonInventario.addProperty("inventarioid", jdbcTemplate.queryForObject("SELECT nextval('inventario_inventarioid_seq') as id;", Long.class));
		jsonInventario.addProperty("inventarioactivo", true);
		jsonInventario.addProperty("inventariofechacreacion", Utils.getFechaActual());
		jsonInventario.addProperty("inventariofechamodificacion", Utils.getFechaActual());
		jsonInventario.addProperty("bitacoraid", -1);
		String filename="INVENTARIO_"+jsonInventario.get("inventarioid").getAsInt()
				+"_"+jsonInventario.get("inventarioimagen").getAsString()+".jpeg";
		jsonInventario.remove("inventarioimagen");
		jsonInventario.addProperty("inventarioimagen", filename);
		Object[] parametros = {
				jsonInventario.get("inventarioid").getAsString(), jsonInventario.get("compraid").getAsString(), 
				jsonInventario.get("tipoproductoid").getAsString(), jsonInventario.get("inventarioactivo").getAsString(), 
				jsonInventario.get("inventariounidad").getAsString(), jsonInventario.get("inventariocostoporunidad").getAsFloat(), 
				jsonInventario.get("inventariocantidadoriginal").getAsFloat(), jsonInventario.get("inventariocantidadactual").getAsFloat(), 
				jsonInventario.get("inventarioimagen").getAsString(), jsonInventario.get("inventariofechacreacion").getAsString(),
				jsonInventario.get("inventariofechamodificacion").getAsString()};
		jdbcTemplate.update("INSERT INTO inventario(inventarioid,compraid,tipoproductoid,inventarioactivo,inventariounidad,inventariocostoporunidad,"
				+"inventariocantidadoriginal,inventariocantidadactual,inventarioimagen,inventariofechacreacion,inventariofechamodificacion) "
				+"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, cast(? as timestamp), cast(? as timestamp))", parametros);
		
		return jsonInventario;
	}

	@Override
	public String SaveImage(MultipartFile multipartFile, Path rootLocation, JsonObject inventario) {
		try {
			log.info("rootLocation >> "+rootLocation.toString());
			if(multipartFile.isEmpty()) {
				throw new RuntimeException("Faile to storage empity file");
			}
			log.info("filename >>" +inventario.get("inventarioimagen").getAsString());
			Path destinationFile = rootLocation.resolve(Paths.get(inventario.get("inventarioimagen").getAsString())).normalize().toAbsolutePath();
			try(InputStream inputStream = multipartFile.getInputStream()){
				Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
			}
	
			return inventario.get("inventarioimagen").getAsString();
		}catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Faile to store file",e);
		}
	}

	@Override
	public Resource getImageInventario(String filename, Path rootLocation) {
		try {
			log.info("filename="+filename);
			log.info("rootLocation="+filename);
			Path file = rootLocation.resolve(filename);
			Resource resource = new UrlResource((file.toUri()));
			if(resource.exists() || resource.isReadable()){
				return resource;
			}else {
				throw new RuntimeException("Could not read file; "+filename);
			}
			
		}catch(MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not read file",e);
		}
	}

	@Override
	public int deleteById(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}
}

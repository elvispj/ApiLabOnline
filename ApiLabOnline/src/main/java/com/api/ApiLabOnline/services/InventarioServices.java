package com.api.ApiLabOnline.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.ApiLabOnline.repository.InventarioRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class InventarioServices {
	private Logger log = LogManager.getLogger(this.getClass());
	
	@Value("${files.location}")
	private String mediaLocation;
	
	private Path rootLocation;
	
	@Autowired
	InventarioRepository inventarioRepository;

	@PostConstruct
	@Autowired
	public void init() throws IOException  {
		log.info("Inicia");
		log.info("mediaLocation == "+mediaLocation);
		rootLocation = Paths.get(mediaLocation);
		Files.createDirectories(rootLocation);
	}

	public JsonArray getAll() {
		return inventarioRepository.getAll();
	}

	public JsonArray getAllTipoProducto() {
		return inventarioRepository.getAllTipoProducto();
	}
	
	public JsonArray list(int limit, int offset){
		return inventarioRepository.list(limit, offset);
	}
	
	public JsonObject getById(Long id) {
		return inventarioRepository.findById(id);
	}

	public JsonObject save(MultipartFile multipartFile, String inventario, HttpServletRequest request) {
		JsonObject jsonInventario = new Gson().fromJson(inventario, JsonObject.class);
		JsonObject response = new JsonObject();
		String path="";
		jsonInventario.remove("inventarioimagen");
		jsonInventario.addProperty("inventarioimagen", new SimpleDateFormat("yyyyMMdd_HHmmss").format(System.currentTimeMillis()));
		log.info("jsonInventario >> "+jsonInventario.toString());
		if(jsonInventario.has("inventarioid") && jsonInventario.get("inventarioid")!=null 
				&& jsonInventario.get("inventarioid").getAsInt()>0)
			response = inventarioRepository.update(jsonInventario.toString());
		else
			response = inventarioRepository.save(jsonInventario.toString());
		
		path= inventarioRepository.SaveImage(multipartFile,rootLocation,response);

		String host = request.getRequestURL().toString().replace(request.getRequestURI(), "");
		String url = ServletUriComponentsBuilder
				.fromHttpUrl(host)
				.path("/media/")
				.path(path)
				.toUriString();
		log.info("path="+path);
		log.info("host="+host);
		log.info("url="+url);
		
		return response;
	}
	
	public JsonObject saveOrUpdate(String inventario) {
		JsonObject jsonInventario = new Gson().fromJson(inventario, JsonObject.class);
		if(jsonInventario.has("inventarioid") && jsonInventario.get("inventarioid")!=null 
				&& jsonInventario.get("inventarioid").getAsInt()>0)
			return inventarioRepository.update(inventario);
		else
			return inventarioRepository.save(inventario);
	}

	public String SaveImage(MultipartFile multipartFile) {
		return inventarioRepository.SaveImage(multipartFile,rootLocation,null);
	}

	public Resource getImageInventario(String filename) {
		return inventarioRepository.getImageInventario(filename,rootLocation);
	}
	
	public int delete(Long id) {
		return inventarioRepository.deleteById(id);
	}
}

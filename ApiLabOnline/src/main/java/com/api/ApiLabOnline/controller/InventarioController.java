package com.api.ApiLabOnline.controller;

import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.ApiLabOnline.services.InventarioServices;
import com.google.gson.Gson;

@RestController
@RequestMapping(path="api/v1/inventario")
public class InventarioController {
	private Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private InventarioServices inventarioServices;
	
	@GetMapping("/all")
	public String getAll(){
		return new Gson().toJson(inventarioServices.getAll());
	}
	
	@GetMapping("/allTipoProducto")
	public String getAllTipoProducto(){
		return new Gson().toJson(inventarioServices.getAllTipoProducto());
	}
	
	@GetMapping("/list")
	public String list(@RequestParam("limit") int limit,@RequestParam("offset") int offset){
		return new Gson().toJson(inventarioServices.list(limit, offset));
	}
	
	@GetMapping("/search/{inventarioid}")
	public String getById(@PathVariable("inventarioid") Long inventarioid){
		return new Gson().toJson(inventarioServices.getById(inventarioid));
	}
	
	@PostMapping("/saveWithImage")
	public String saveWithImage(HttpServletRequest request, 
			@RequestParam("inventarioimagen") MultipartFile multipartFile,
			@RequestParam("inventario") String jsonInventario){
		log.info("Salva inventario ");
		return new Gson().toJson(inventarioServices.save(multipartFile, jsonInventario, request));
	}
	
	@PostMapping("/save")
	public String saveUpdate(@RequestBody String inventario){
		log.info("Salva inventario "+inventario);
		return new Gson().toJson(inventarioServices.saveOrUpdate(inventario));
	}
	
	@GetMapping("/image/{filename}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) throws IOException{
		Resource file = inventarioServices.getImageInventario(filename);
		String contentType = Files.probeContentType(file.getFile().toPath());
		
		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_TYPE, contentType)
				.body(file);		
	}
	
	@DeleteMapping("/delete/{inventarioid}")
	public void delete(@PathVariable("inventarioid") Long inventarioid){
		inventarioServices.delete(inventarioid);
	}

}

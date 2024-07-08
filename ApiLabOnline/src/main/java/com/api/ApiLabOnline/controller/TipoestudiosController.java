package com.api.ApiLabOnline.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.ApiLabOnline.services.TipoestudiosService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@RestController
@RequestMapping(path="api/v1/tipoestudios")
public class TipoestudiosController {
	@Autowired
	private TipoestudiosService tipoestudiosService;
	
	private Logger log = LogManager.getLogger(TipoestudiosController.class);
	
	@GetMapping("/all/")
	public String getAll(){
		log.info("get all");
		return new Gson().toJson(tipoestudiosService.all());
	}
	
	@GetMapping("/list/")
	public String getList(@RequestParam("limit") int limit,@RequestParam("offset") int offset){
		log.info("Si llego all limit["+limit+"] offset["+offset+"]");
		return new Gson().toJson(tipoestudiosService.list(limit, offset));
	}
	
	@GetMapping("/search/{tipoestudioid}")
	public String getById(@PathVariable("tipoestudioid") Long tipoestudioid){
		log.info("Si llego byid "+tipoestudioid);
		JsonObject orden =tipoestudiosService.findById(tipoestudioid); 
		return new Gson().toJson(orden);
	}
	
	@PostMapping("/save")
	public String saveUpdate(@RequestBody String jsonTipoestudio){
		log.info("Si llego save "+jsonTipoestudio);
		JsonObject nuevaorden =tipoestudiosService.save(jsonTipoestudio);
		log.info("Nueva orden == "+nuevaorden.toString());
		return new Gson().toJson(nuevaorden);
	}

}

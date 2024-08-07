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

import com.api.ApiLabOnline.services.DoctoresService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@RestController
@RequestMapping(path="api/v1/doctores")
public class DoctoresController {
	@Autowired
	private DoctoresService doctoresService;
	
	private Logger log = LogManager.getLogger(DoctoresController.class);
	
	@GetMapping("/all/")
	public String getAll(){
		log.info("get all");
		return new Gson().toJson(doctoresService.all());
	}
	
	@GetMapping("/list/")
	public String getList(@RequestParam("limit") int limit,@RequestParam("offset") int offset){
		log.info("get list");
		return new Gson().toJson(doctoresService.list(limit, offset));
	}
	
	@GetMapping("/searchByUsuarioid/{usuarioid}")
	public String getByUsuarioid(@PathVariable("usuarioid") Long usuarioid){
		log.info("Search ByUsuarioId "+usuarioid);
		return new Gson().toJson(doctoresService.findByUsuarioId(usuarioid));
	}
	
	@GetMapping("/search/{doctorid}")
	public String getById(@PathVariable("doctorid") Long doctorid){
		log.info("Search ById "+doctorid);
		JsonObject orden =doctoresService.findById(doctorid); 
		return new Gson().toJson(orden);
	}
	
	@PostMapping("/save")
	public String saveUpdate(@RequestBody String jsonDoctor){
		log.info("Save "+jsonDoctor);
		return new Gson().toJson(doctoresService.save(jsonDoctor));
	}

}

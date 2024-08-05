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

import com.api.ApiLabOnline.services.CitasService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@RestController
@RequestMapping(path="api/v1/citas")
public class DoctorCitasController {
	@Autowired
	private CitasService citasService;
	
	private Logger log = LogManager.getLogger(DoctorCitasController.class);
	
	@GetMapping("/all/")
	public String getAll(){
		log.info("get all");
		return new Gson().toJson(citasService.all());
	}
	
	@GetMapping("/list/")
	public String getList(@RequestParam("limit") int limit,@RequestParam("offset") int offset){
		log.info("get list");
		return new Gson().toJson(citasService.list(limit, offset));
	}
	
	@GetMapping("/listByDoctorid/{doctorid}")
	public String getByDoctorid(@PathVariable("doctorid") Long doctorid){
		log.info("Search getByDoctorid "+doctorid);
		return new Gson().toJson(citasService.listByDoctorid(doctorid));
	}
	
	@GetMapping("/search/{citaid}")
	public String getById(@PathVariable("citaid") Long citaid){
		log.info("Search ById "+citaid);
		JsonObject orden =citasService.getById(citaid);
		return new Gson().toJson(orden);
	}
	
	@PostMapping("/save")
	public String saveUpdate(@RequestBody String jsonCita){
		log.info("Save "+jsonCita);
		return new Gson().toJson(citasService.save(jsonCita));
	}

}

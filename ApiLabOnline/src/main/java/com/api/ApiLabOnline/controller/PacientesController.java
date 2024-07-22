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

import com.api.ApiLabOnline.services.PacientesService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@RestController
@RequestMapping(path="api/v1/pacientes")
public class PacientesController {
	@Autowired
	private PacientesService pacientesService;
	
	private Logger log = LogManager.getLogger(this.getClass());
	
	@GetMapping("/all/")
	public String getAll(){
		log.info("get all");
		return new Gson().toJson(pacientesService.all());
	}
	
	@GetMapping("/list/")
	public String getList(@RequestParam("limit") int limit,@RequestParam("offset") int offset){
		log.info("get list");
		return new Gson().toJson(pacientesService.list(limit, offset));
	}
	
	@GetMapping("/listByDoctorid/{doctorid}")
	public String getByDoctorid(@PathVariable("doctorid") Long doctorid){
		log.info("Search getByDoctorid "+doctorid);
		return new Gson().toJson(pacientesService.listByDoctorid(doctorid));
	}
	
	@GetMapping("/search/{pacienteid}")
	public String getById(@PathVariable("pacienteid") Long pacienteid){
		log.info("Search ById "+pacienteid);
		JsonObject orden =pacientesService.findById(pacienteid); 
		return new Gson().toJson(orden);
	}
	
	@PostMapping("/save")
	public String saveUpdate(@RequestBody String jsonPaciente){
		log.info("Save "+jsonPaciente);
		return new Gson().toJson(pacientesService.save(jsonPaciente));
	}

}

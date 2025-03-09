package com.api.ApiLabOnline.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.ApiLabOnline.services.MensajesService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@RestController
@RequestMapping(path="api/v1/mensajes")
public class MensajesController {
	@Autowired
	private MensajesService mensajesService;
	
	private Logger log = LogManager.getLogger(MensajesController.class);
	
	@GetMapping("/listMensajetipos/")
	public String getMensajetipos(){
		log.info("get listMensajetipos");
		return new Gson().toJson(mensajesService.getMensajetipos());
	}
	
	@GetMapping("/listByClienteid/{clienteid}")
	public String getByClienteid(@PathVariable("clienteid") Long clienteid){
		log.info("Search getByDoctorid "+clienteid);
		return new Gson().toJson(mensajesService.listByClienteid(clienteid));
	}
	
	@GetMapping("/listByDoctorid/{doctorid}")
	public String getByDoctorid(@PathVariable("doctorid") Long doctorid){
		log.info("Search getByDoctorid "+doctorid);
		return new Gson().toJson(mensajesService.listByDoctorid(doctorid));
	}
	
	@GetMapping("/search/{mensajeid}")
	public String getById(@PathVariable("mensajeid") Long mensajeid){
		log.info("Search ById "+mensajeid);
		JsonObject orden =mensajesService.getById(mensajeid);
		return new Gson().toJson(orden);
	}
	
	@PostMapping("/save")
	public String saveUpdate(@RequestBody String jsonMensaje){
		log.info("Save "+jsonMensaje);
		return new Gson().toJson(mensajesService.save(jsonMensaje));
	}

}

package com.api.ApiLabOnline.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.ApiLabOnline.services.OrdenesService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@RestController
@RequestMapping(path="api/v1/ordenes")
@CrossOrigin("*")
public class OrdenesController {
	@Autowired
	private OrdenesService ordenesService;
	
	@PostMapping("/list")
	public String getAllPDataTable(){
		JsonObject response = new JsonObject();
		JsonArray list = ordenesService.listOrdenes(10, 1);
		response.addProperty("data", (list!=null ? list.toString() : "[]"));
		response.addProperty("totalRecords", (list!=null ? list.size() : 0));
		response.addProperty("filteredRecords", "");
		return new Gson().toJson(response);
	}
	
	@GetMapping("/list")
	public String getAll(){
		return new Gson().toJson(ordenesService.listOrdenes(10000, 1));
	}
	
	@GetMapping("/search/{ordenid}")
	public String getById(@PathVariable("ordenid") Long ordenid){
		return new Gson().toJson(ordenesService.findById(ordenid));
	}
	
	@PostMapping("/save")
	public String saveUpdate(@RequestBody String jsonOrden){
		return new Gson().toJson(ordenesService.save(jsonOrden));
	}
	
	@PostMapping("/saveOrden")
	public String saveUpdateOrden(@RequestBody String jsonOrden){
		return new Gson().toJson(ordenesService.saveOrden(jsonOrden));
	}
}

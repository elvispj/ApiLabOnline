package com.api.ApiLabOnline.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@GetMapping("/list")
	public String getAll(){
		System.out.println("Si llego all ");
		JsonArray list = ordenesService.listOrdenes(10000, 1);
		if(list!=null)
			System.out.println("Encontro "+list.size()+" elementos");
		return list.toString();
	}
	
	@GetMapping("/search/{ordenid}")
	public String getById(@PathVariable("ordenid") Long ordenid){
		System.out.println("Si llego byid "+ordenid);
		JsonObject orden =ordenesService.findById(ordenid); 
		return new Gson().toJson(orden);
	}
	
	@PostMapping("/save")
	public String saveUpdate(@RequestBody String jsonOrden){
		System.out.println("Si llego save "+jsonOrden);
		JsonObject nuevaorden =ordenesService.save(jsonOrden);
		System.out.println("Nueva orden == "+nuevaorden.toString());
		return new Gson().toJson(nuevaorden);
	}
}

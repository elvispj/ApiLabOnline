package com.api.ApiLabOnline.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.ApiLabOnline.services.EstudiosServices;
import com.google.gson.Gson;

@RestController
@RequestMapping(path="api/v1/estudios")
@CrossOrigin("*")
public class EstudiosController {
	@Autowired
	private EstudiosServices estudiosServices;
	
	@PostMapping("/all")
	public String getAll(){
		return new Gson().toJson(estudiosServices.getAll());
	}
	
	@GetMapping("/list")
	public String listEstudios(@RequestParam("limit") int limit,@RequestParam("offset") int offset){
		return new Gson().toJson(estudiosServices.listEstudios(limit, offset));
	}
	
	@GetMapping("/search/{estudioid}")
	public String getById(@PathVariable("estudioid") Long estudioid){
		return new Gson().toJson(estudiosServices.getEstudio(estudioid));
	}
	
	@PostMapping("/save")
	public String saveUpdate(@RequestBody String estudio){
		return new Gson().toJson(estudiosServices.saveOrUpdate(estudio));
	}
	
	@DeleteMapping("/delete/{estudioid}")
	public void delete(@PathVariable("estudioid") Long estudioid){
		estudiosServices.delete(estudioid);
	}
}

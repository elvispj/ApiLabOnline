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
import com.google.gson.JsonArray;

@RestController
@RequestMapping(path="api/v1/estudios")
@CrossOrigin("*")
public class EstudiosController {
	@Autowired
	private EstudiosServices estudiosServices;
	
	@GetMapping("/all")
	public String getAll(){
		JsonArray list = estudiosServices.getAll();
		if(list!=null)
			System.out.println("Encontro "+list.size()+" estudios");
		return list.toString();
	}
	
	@GetMapping("/list")
	public String listEstudios(@RequestParam("limit") int limit,@RequestParam("offset") int offset){
//		return estudiosServices.listEstudios(10,0);
		System.out.println("Si llego all limit["+limit+"] offset["+offset+"]");
		JsonArray list = estudiosServices.listEstudios(limit, offset);
		if(list!=null)
			System.out.println("Encontro "+list.size()+" estudios");
		return list.toString();
	}
	
	@GetMapping("/search/{estudioid}")
	public String getById(@PathVariable("estudioid") Long estudioid){
		return estudiosServices.getEstudio(estudioid).toString();
	}
	
	@PostMapping("/save")
	public String saveUpdate(@RequestBody String estudio){
		estudiosServices.saveOrUpdate(estudio);
		return estudio;
	}
	
	@DeleteMapping("/delete/{estudioid}")
	public void delete(@PathVariable("estudioid") Long estudioid){
		estudiosServices.delete(estudioid);
	}
}

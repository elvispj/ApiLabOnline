package com.api.ApiLabOnline.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.ApiLabOnline.services.InventarioServices;
import com.google.gson.JsonArray;

@RestController
@RequestMapping(path="api/v1/inventario")
public class InventarioController {
	@Autowired
	private InventarioServices inventarioServices;
	
	@GetMapping("/all")
	public String getAll(){
		JsonArray list = inventarioServices.getAll();
		if(list!=null)
			System.out.println("Encontro "+list.size()+" inventario");
		return list.toString();
	}
	
	@GetMapping("/list")
	public String list(@RequestParam("limit") int limit,@RequestParam("offset") int offset){
//		return estudiosServices.listEstudios(10,0);
		System.out.println("Si llego all limit["+limit+"] offset["+offset+"]");
		JsonArray list = inventarioServices.list(limit, offset);
		if(list!=null)
			System.out.println("Encontro "+list.size()+" inventario");
		return list.toString();
	}
	
	@GetMapping("/search/{inventarioid}")
	public String getById(@PathVariable("inventarioid") Long inventarioid){
		return inventarioServices.getById(inventarioid).toString();
	}
	
	@PostMapping("/save")
	public String saveUpdate(@RequestBody String inventario){
		inventarioServices.saveOrUpdate(inventario);
		return inventario;
	}
	
	@DeleteMapping("/delete/{inventarioid}")
	public void delete(@PathVariable("inventarioid") Long inventarioid){
		inventarioServices.delete(inventarioid);
	}

}

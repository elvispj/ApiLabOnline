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
import com.google.gson.Gson;

@RestController
@RequestMapping(path="api/v1/inventario")
public class InventarioController {
	@Autowired
	private InventarioServices inventarioServices;
	
	@GetMapping("/all")
	public String getAll(){
		return new Gson().toJson(inventarioServices.getAll());
	}
	
	@GetMapping("/allTipoProducto")
	public String getAllTipoProducto(){
		return new Gson().toJson(inventarioServices.getAllTipoProducto());
	}
	
	@GetMapping("/list")
	public String list(@RequestParam("limit") int limit,@RequestParam("offset") int offset){
		return new Gson().toJson(inventarioServices.list(limit, offset));
	}
	
	@GetMapping("/search/{inventarioid}")
	public String getById(@PathVariable("inventarioid") Long inventarioid){
		return new Gson().toJson(inventarioServices.getById(inventarioid));
	}
	
	@PostMapping("/save")
	public String saveUpdate(@RequestBody String inventario){
		return new Gson().toJson(inventarioServices.saveOrUpdate(inventario));
	}
	
	@DeleteMapping("/delete/{inventarioid}")
	public void delete(@PathVariable("inventarioid") Long inventarioid){
		inventarioServices.delete(inventarioid);
	}

}

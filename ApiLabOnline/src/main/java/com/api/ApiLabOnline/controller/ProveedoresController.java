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

import com.api.ApiLabOnline.services.ProveedoresServices;
import com.google.gson.Gson;

@RestController
@RequestMapping(path="api/v1/proveedores")
public class ProveedoresController {
	@Autowired
	private ProveedoresServices proveedoresServices;
	
	@GetMapping("/all")
	public String getAll(){
		return new Gson().toJson(proveedoresServices.getAll());
	}
	
	@GetMapping("/list")
	public String list(@RequestParam("limit") int limit,@RequestParam("offset") int offset){
		return new Gson().toJson(proveedoresServices.list(limit, offset));
	}
	
	@GetMapping("/search/{proveedorid}")
	public String getById(@PathVariable("proveedorid") Long proveedorid){
		return new Gson().toJson(proveedoresServices.getById(proveedorid));
	}
	
	@PostMapping("/save")
	public String saveUpdate(@RequestBody String proveedor){
		return new Gson().toJson(proveedoresServices.saveOrUpdate(proveedor));
	}
	
	@DeleteMapping("/delete/{proveedorid}")
	public void delete(@PathVariable("proveedorid") Long proveedorid){
		proveedoresServices.delete(proveedorid);
	}

}

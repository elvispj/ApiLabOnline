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
import com.google.gson.JsonArray;

@RestController
@RequestMapping(path="api/v1/proveedores")
public class ProveedoresController {
	@Autowired
	private ProveedoresServices proveedoresServices;
	
	@GetMapping("/all")
	public String getAll(){
		JsonArray list = proveedoresServices.getAll();
		if(list!=null)
			System.out.println("Encontro "+list.size()+" compras");
		return list.toString();
	}
	
	@GetMapping("/list")
	public String list(@RequestParam("limit") int limit,@RequestParam("offset") int offset){
		System.out.println("Si llego all limit["+limit+"] offset["+offset+"]");
		JsonArray list = proveedoresServices.list(limit, offset);
		if(list!=null)
			System.out.println("Encontro "+list.size()+" proveedores");
		return list.toString();
	}
	
	@GetMapping("/search/{proveedorid}")
	public String getById(@PathVariable("proveedorid") Long proveedorid){
		return proveedoresServices.getById(proveedorid).toString();
	}
	
	@PostMapping("/save")
	public String saveUpdate(@RequestBody String proveedor){
		proveedoresServices.saveOrUpdate(proveedor);
		return proveedor;
	}
	
	@DeleteMapping("/delete/{proveedorid}")
	public void delete(@PathVariable("proveedorid") Long proveedorid){
		proveedoresServices.delete(proveedorid);
	}

}

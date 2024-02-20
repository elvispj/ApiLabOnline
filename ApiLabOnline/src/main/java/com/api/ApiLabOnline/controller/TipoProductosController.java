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

import com.api.ApiLabOnline.services.TipoProductosServices;
import com.google.gson.JsonArray;

@RestController
@RequestMapping(path="api/v1/tipoproducto")
public class TipoProductosController {
	@Autowired
	private TipoProductosServices tipoProductosServices;
	
	@GetMapping("/all")
	public String getAll(){
		JsonArray list = tipoProductosServices.getAll();
		if(list!=null)
			System.out.println("Encontro "+list.size()+" tipos de producto");
		return list.toString();
	}
	
	@GetMapping("/list")
	public String list(@RequestParam("limit") int limit,@RequestParam("offset") int offset){
//		return estudiosServices.listEstudios(10,0);
		System.out.println("Si llego all limit["+limit+"] offset["+offset+"]");
		JsonArray list = tipoProductosServices.list(limit, offset);
		if(list!=null)
			System.out.println("Encontro "+list.size()+" tipos de producto");
		return list.toString();
	}
	
	@GetMapping("/search/{tipoproductoid}")
	public String getById(@PathVariable("tipoproductoid") Long tipoproductoid){
		return tipoProductosServices.getById(tipoproductoid).toString();
	}
	
	@PostMapping("/save")
	public String saveUpdate(@RequestBody String compra){
		tipoProductosServices.saveOrUpdate(compra);
		return compra;
	}
	
	@DeleteMapping("/delete/{tipoproductoid}")
	public void delete(@PathVariable("tipoproductoid") Long tipoproductoid){
		tipoProductosServices.delete(tipoproductoid);
	}

}

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
import com.google.gson.Gson;

@RestController
@RequestMapping(path="api/v1/tipoproducto")
public class TipoProductosController {
	@Autowired
	private TipoProductosServices tipoProductosServices;
	
	@GetMapping("/all")
	public String getAll(){
		return new Gson().toJson(tipoProductosServices.getAll());
	}
	
	@GetMapping("/list")
	public String list(@RequestParam("limit") int limit,@RequestParam("offset") int offset){
		return new Gson().toJson(tipoProductosServices.list(limit, offset));
	}
	
	@GetMapping("/search/{tipoproductoid}")
	public String getById(@PathVariable("tipoproductoid") Long tipoproductoid){
		return new Gson().toJson(tipoProductosServices.getById(tipoproductoid));
	}
	
	@PostMapping("/save")
	public String saveUpdate(@RequestBody String compra){
		return new Gson().toJson(tipoProductosServices.saveOrUpdate(compra));
	}
	
	@DeleteMapping("/delete/{tipoproductoid}")
	public void delete(@PathVariable("tipoproductoid") Long tipoproductoid){
		tipoProductosServices.delete(tipoproductoid);
	}

}

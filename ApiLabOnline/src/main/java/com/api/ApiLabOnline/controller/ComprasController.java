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

import com.api.ApiLabOnline.services.ComprasServices;
import com.google.gson.JsonArray;

@RestController
@RequestMapping(path="api/v1/compras")
public class ComprasController {
	@Autowired
	private ComprasServices comprasServices;
	
	@GetMapping("/all")
	public String getAll(){
		JsonArray list = comprasServices.getAll();
		if(list!=null)
			System.out.println("Encontro "+list.size()+" compras");
		return list.toString();
	}
	
	@GetMapping("/list")
	public String list(@RequestParam("limit") int limit,@RequestParam("offset") int offset){
//		return estudiosServices.listEstudios(10,0);
		System.out.println("Si llego all limit["+limit+"] offset["+offset+"]");
		JsonArray list = comprasServices.list(limit, offset);
		if(list!=null)
			System.out.println("Encontro "+list.size()+" compras");
		return list.toString();
	}
	
	@GetMapping("/search/{compraid}")
	public String getById(@PathVariable("compraid") Long compraid){
		return comprasServices.getById(compraid).toString();
	}
	
	@PostMapping("/save")
	public String saveUpdate(@RequestBody String compra){
		comprasServices.saveOrUpdate(compra);
		return compra;
	}
	
	@DeleteMapping("/delete/{compraid}")
	public void delete(@PathVariable("compraid") Long compraid){
		comprasServices.delete(compraid);
	}

}

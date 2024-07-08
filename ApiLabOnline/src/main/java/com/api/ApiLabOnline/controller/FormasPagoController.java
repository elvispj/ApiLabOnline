package com.api.ApiLabOnline.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.ApiLabOnline.services.FormasPagoServices;
import com.google.gson.Gson;

@RestController
@RequestMapping(path="api/v1/formaspago")
public class FormasPagoController {
	@Autowired
	private FormasPagoServices formasPagoServices;
	
	@GetMapping("/all")
	public String getAll(){
		return new Gson().toJson(formasPagoServices.getAll());
	}
	
	@GetMapping("/search/{tipoproductoid}")
	public String getById(@PathVariable("tipoproductoid") String formapagoid){
		return new Gson().toJson(formasPagoServices.getById(formapagoid));
	}
	
	@PostMapping("/save")
	public String saveUpdate(@RequestBody String formapago){
		return new Gson().toJson(formasPagoServices.saveOrUpdate(formapago));
	}
	
	@DeleteMapping("/delete/{formapagoid}")
	public void delete(@PathVariable("formapagoid") Long formapagoid){
		formasPagoServices.delete(formapagoid);
	}

}

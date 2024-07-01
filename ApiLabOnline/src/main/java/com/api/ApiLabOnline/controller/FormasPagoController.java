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
import com.google.gson.JsonArray;

@RestController
@RequestMapping(path="api/v1/formaspago")
public class FormasPagoController {
	@Autowired
	private FormasPagoServices formasPagoServices;
	
	@GetMapping("/all")
	public String getAll(){
		JsonArray list = formasPagoServices.getAll();
		if(list!=null)
			System.out.println("Encontro "+list.size()+" formas pago");
		return list.toString();
	}
	
	@GetMapping("/search/{tipoproductoid}")
	public String getById(@PathVariable("tipoproductoid") String formapagoid){
		return formasPagoServices.getById(formapagoid).toString();
	}
	
	@PostMapping("/save")
	public String saveUpdate(@RequestBody String formapago){
		formasPagoServices.saveOrUpdate(formapago);
		return formapago;
	}
	
	@DeleteMapping("/delete/{formapagoid}")
	public void delete(@PathVariable("formapagoid") Long formapagoid){
		formasPagoServices.delete(formapagoid);
	}

}

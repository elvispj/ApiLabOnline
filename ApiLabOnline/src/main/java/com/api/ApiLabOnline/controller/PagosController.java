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

import com.api.ApiLabOnline.services.PagosServices;
import com.google.gson.Gson;

@RestController
@RequestMapping(path="api/v1/pagos")
public class PagosController {
	@Autowired
	private PagosServices pagosServices;
	
	@GetMapping("/all")
	public String getAll(){
		return pagosServices.getAll().toString();
	}
	
	@GetMapping("/list")
	public String list(@RequestParam("limit") int limit,@RequestParam("offset") int offset){
		return new Gson().toJson(pagosServices.list(limit, offset));
	}
	
	@GetMapping("/search/orden/{ordenid}")
	public String getPagoByOrdenId(@PathVariable("ordenid") Long ordenid){
		return new Gson().toJson(pagosServices.getPagoByOrdenId(ordenid));
	}
	
	@GetMapping("/search/{pagoid}")
	public String getById(@PathVariable("pagoid") Long pagoid){
		return new Gson().toJson(pagosServices.getById(pagoid));
	}
	
	@PostMapping("/save")
	public String saveUpdate(@RequestBody String pago){
		pagosServices.saveOrUpdate(pago);
		return pago;
	}
	
	@DeleteMapping("/delete/{pagoid}")
	public void delete(@PathVariable("pagoid") Long pagoid){
		pagosServices.delete(pagoid);
	}

}

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
		return pagosServices.list(limit, offset).toString();
	}
	
	@GetMapping("/search/orden/{ordenid}")
	public String getPagoByOrdenId(@PathVariable("ordenid") Long ordenid){
		return pagosServices.getPagoByOrdenId(ordenid).toString();
	}
	
	@GetMapping("/search/{pagoid}")
	public String getById(@PathVariable("pagoid") Long pagoid){
		return pagosServices.getById(pagoid).toString();
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

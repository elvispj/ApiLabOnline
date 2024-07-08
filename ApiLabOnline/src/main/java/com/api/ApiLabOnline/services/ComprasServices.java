package com.api.ApiLabOnline.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.repository.ComprasRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class ComprasServices {
	@Autowired
	ComprasRepository comprasRepository;

	public JsonArray getAll() {
		return comprasRepository.getAll();
	}
	
	public JsonArray list(int limit, int offset){
		return comprasRepository.list(limit, offset);
	}
	
	public JsonObject getById(Long id) {
		return comprasRepository.findById(id);
	}
	
	public JsonObject saveOrUpdate(String compra) {
		JsonObject jsonCompra = new Gson().fromJson(compra, JsonObject.class);
		if(jsonCompra.has("compraid") && jsonCompra.get("compraid")!=null 
				&& jsonCompra.get("compraid").getAsInt()>0)
			return comprasRepository.update(compra);
		else
			return comprasRepository.save(compra);
		
	}
	
	public int delete(Long id) {
		return comprasRepository.deleteById(id);
	}
}

package com.api.ApiLabOnline.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.repository.ProveedoresRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class ProveedoresServices {
	@Autowired
	ProveedoresRepository proveedoresRepository;

	public JsonArray getAll() {
		return proveedoresRepository.getAll();
	}
	
	public JsonArray list(int limit, int offset){
		return proveedoresRepository.list(limit, offset);
	}
	
	public JsonObject getById(Long id) {
		return proveedoresRepository.findById(id);
	}
	
	public void saveOrUpdate(String proveedor) {
		JsonObject jsonCompra = new Gson().fromJson(proveedor, JsonObject.class);
		if(jsonCompra.has("proveedorid") && jsonCompra.get("proveedorid")!=null 
				&& jsonCompra.get("proveedorid").getAsInt()>0)
			proveedoresRepository.update(proveedor);
		else
			proveedoresRepository.save(proveedor);
		
	}
	
	public int delete(Long id) {
		return proveedoresRepository.deleteById(id);
	}


}

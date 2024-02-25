package com.api.ApiLabOnline.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.repository.InventarioRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class InventarioServices {
	@Autowired
	InventarioRepository inventarioRepository;

	public JsonArray getAll() {
		return inventarioRepository.getAll();
	}

	public JsonArray getAllTipoProducto() {
		return inventarioRepository.getAllTipoProducto();
	}
	
	public JsonArray list(int limit, int offset){
		return inventarioRepository.list(limit, offset);
	}
	
	public JsonObject getById(Long id) {
		return inventarioRepository.findById(id);
	}
	
	public void saveOrUpdate(String inventario) {
		JsonObject jsonInventario = new Gson().fromJson(inventario, JsonObject.class);
		if(jsonInventario.has("inventarioid") && jsonInventario.get("inventarioid")!=null 
				&& jsonInventario.get("inventarioid").getAsInt()>0)
			inventarioRepository.update(inventario);
		else
			inventarioRepository.save(inventario);
		
	}
	
	public int delete(Long id) {
		return inventarioRepository.deleteById(id);
	}
}

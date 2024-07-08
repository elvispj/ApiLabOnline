package com.api.ApiLabOnline.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.repository.TipoProductosRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class TipoProductosServices {
	@Autowired
	TipoProductosRepository tipoProductosRepository;

	public JsonArray getAll() {
		return tipoProductosRepository.getAll();
	}
	
	public JsonArray list(int limit, int offset){
		return tipoProductosRepository.list(limit, offset);
	}
	
	public JsonObject getById(Long id) {
		return tipoProductosRepository.findById(id);
	}
	
	public JsonObject saveOrUpdate(String tipoproducto) {
		JsonObject jsonTipoProducto = new Gson().fromJson(tipoproducto, JsonObject.class);
		if(jsonTipoProducto.has("tipoproductoid") && jsonTipoProducto.get("tipoproductoid")!=null 
				&& jsonTipoProducto.get("tipoproductoid").getAsInt()>0)
			return tipoProductosRepository.update(tipoproducto);
		else
			return tipoProductosRepository.save(tipoproducto);
		
	}
	
	public int delete(Long id) {
		return tipoProductosRepository.deleteById(id);
	}
}

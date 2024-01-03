package com.api.ApiLabOnline.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.repository.OrdenesRepository;
import com.api.ApiLabOnline.repository.OrdenesdetalleRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class OrdenesService {

	@Autowired
	private OrdenesRepository ordenesRepository;
	@Autowired
	private OrdenesdetalleRepository ordenedetalleRepository;

	public JsonArray listOrdenes(int limit, int offset) {
		return ordenesRepository.listOrdenes(limit, offset);
	}

	public JsonObject findById(Long id){
		return ordenesRepository.findById(id);
	}

	public JsonObject save(String jsonOrden) {
		JsonObject res = ordenesRepository.save(jsonOrden);
		if(res!=null && res.get("ordenid")!=null 
				&& res.get("ordenid").getAsInt()>0) {
			if(ordenedetalleRepository.save(res.getAsString())!=null) {
				return res;
			}
		}
		return new JsonObject();
	}

	public int deleteById(Long id) {
		return ordenesRepository.deleteById(id);
	}
}

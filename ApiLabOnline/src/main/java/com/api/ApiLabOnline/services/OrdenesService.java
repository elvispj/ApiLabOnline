package com.api.ApiLabOnline.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.repository.OrdenesRepository;
import com.api.ApiLabOnline.repository.OrdenesdetalleRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
			JsonArray listaOrdenesDetalle=res.get("ordenesdetalle").getAsJsonArray();
			if(listaOrdenesDetalle==null || listaOrdenesDetalle.size()<1) {
				System.out.println("La lista de ordendetalle esta vacia");
				return null;
			}
			for(JsonElement ordendetalle: listaOrdenesDetalle) {
				ordenedetalleRepository.save(ordendetalle.getAsString());
			}
			return res;
		}
		return null;
	}

	public int deleteById(Long id) {
		return ordenesRepository.deleteById(id);
	}
}

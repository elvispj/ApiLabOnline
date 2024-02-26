package com.api.ApiLabOnline.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.repository.ClientesRepository;
import com.api.ApiLabOnline.repository.OrdenesRepository;
import com.api.ApiLabOnline.repository.OrdenesdetalleRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Service
public class OrdenesService {

	@Autowired
	private OrdenesRepository ordenesRepository;
	@Autowired
	private OrdenesdetalleRepository ordenedetalleRepository;
	@Autowired
	private ClientesRepository clientesRepository;

	public JsonArray listOrdenes(int limit, int offset) {
		return ordenesRepository.listOrdenes(limit, offset);
	}

	public JsonObject findById(Long id){
		return ordenesRepository.findById(id);
	}

	public JsonObject save(String jsonOrden) {
		JsonObject orden = new Gson().fromJson(jsonOrden, JsonObject.class);
		
		if(orden.get("clienteid")==null || orden.get("clienteid").getAsString().equals("")
				|| orden.get("clienteid").getAsInt()<1) {
			JsonObject cliente = clientesRepository.save(orden.get("cliente").getAsJsonObject().toString());
			orden.addProperty("clienteid", cliente.get("clienteid").getAsInt());
		}
		orden = ordenesRepository.save(orden.getAsJsonObject().toString());
		if(orden!=null && orden.get("ordenid")!=null 
				&& orden.get("ordenid").getAsInt()>0) {
			JsonArray listaOrdenesDetalle=orden.get("ordenesdetalle").getAsJsonArray();
			if(listaOrdenesDetalle==null || listaOrdenesDetalle.size()<1) {
				System.out.println("La lista de ordendetalle esta vacia");
				return null;
			}
			for(JsonElement ordendetalle: listaOrdenesDetalle) {
				ordendetalle.getAsJsonObject().addProperty("ordenid", orden.get("ordenid").getAsInt());
				ordenedetalleRepository.save(ordendetalle.getAsJsonObject().toString());
			}
			return orden;
		}
		return null;
	}

	public int deleteById(Long id) {
		return ordenesRepository.deleteById(id);
	}
}

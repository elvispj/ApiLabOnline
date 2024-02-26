package com.api.ApiLabOnline.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.repository.ClientesRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class ClientesService {

	@Autowired
	private ClientesRepository clientesRepository;

	public JsonArray all() {
		return clientesRepository.all();
	}

	public JsonArray list(int limit, int offset) {
		return clientesRepository.list(limit, offset);
	}

	public JsonObject findById(Long clienteid) {
		return clientesRepository.findById(clienteid);
	}

	public JsonArray findByLike(String parametro) {
		return clientesRepository.findByLike(parametro);
	}

	public JsonObject save(String jsonCliente) {
		JsonObject cliente = new Gson().fromJson(jsonCliente, JsonObject.class);
		if(cliente.has("clienteid") && cliente.get("clienteid")!=null 
				&& cliente.get("clienteid").getAsInt()>0)
			return clientesRepository.update(jsonCliente);
		else
			return clientesRepository.save(jsonCliente);
	}
}

package com.api.ApiLabOnline.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.repository.MensajesRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class MensajesService {

	@Autowired
	private MensajesRepository mensajesRepository;

	public JsonArray listByClienteid(Long pacienteid) {
		return mensajesRepository.listByClienteid(pacienteid);
	}

	public JsonArray listByDoctorid(Long doctorid) {
		JsonArray listaCitas = mensajesRepository.listByDoctorid(doctorid);
		return listaCitas;
	}

	public JsonObject getById(Long mensajeid) {
		return mensajesRepository.findById(mensajeid);
	}

	public JsonObject save(String jsonMensaje) {
		JsonObject cita = new Gson().fromJson(jsonMensaje, JsonObject.class);
		if(cita.has("mensajeid") && cita.get("mensajeid")!=null 
				&& cita.get("mensajeid").getAsInt()>0)
			return mensajesRepository.update(jsonMensaje);
		else
			return mensajesRepository.save(jsonMensaje);
	}
	
}

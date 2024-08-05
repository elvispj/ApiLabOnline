package com.api.ApiLabOnline.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.repository.CitasRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class CitasService {

	@Autowired
	private CitasRepository citasRepository;
	
	public JsonArray all() {
		return citasRepository.all();
	}

	public JsonArray list(int limit, int offset) {
		return citasRepository.list(limit, offset);
	}

	public JsonArray listByDoctorid(Long doctorid) {
		JsonArray listaCitas = citasRepository.listByDoctorid(doctorid);
//		if(doctor!=null)
//			doctor.add("especialidades", especialidadesService.listByDoctorid(doctor.get("doctorid").getAsLong()));
		return listaCitas;
	}

	public JsonObject getById(Long citaid) {
		return citasRepository.findById(citaid);
	}

	public JsonObject save(String jsonCita) {
		JsonObject cita = new Gson().fromJson(jsonCita, JsonObject.class);
		if(cita.has("citaid") && cita.get("citaid")!=null 
				&& cita.get("citaid").getAsInt()>0)
			return citasRepository.update(jsonCita);
		else
			return citasRepository.save(jsonCita);
	}
	
}

package com.api.ApiLabOnline.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.repository.DoctoresRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class DoctoresService {

	@Autowired
	private DoctoresRepository doctoresRepository;

	@Autowired
	private EspecialidadesService especialidadesService;

	public JsonArray all() {
		return doctoresRepository.all();
	}

	public JsonArray list(int limit, int offset) {
		return doctoresRepository.list(limit, offset);
	}

	public JsonObject findById(Long doctorid) {
		return doctoresRepository.findById(doctorid);
	}

	public JsonObject findByUsuarioId(Long usuarioid) {
		JsonObject doctor = doctoresRepository.findByUsuarioId(usuarioid);
		doctor.add("especialidades", especialidadesService.listByDoctorid(doctor.get("doctorid").getAsLong()));
		return doctor;
	}

	public JsonObject save(String jsonDoctor) {
		JsonObject doctor = new Gson().fromJson(jsonDoctor, JsonObject.class);
		if(doctor.has("doctorid") && doctor.get("doctorid")!=null 
				&& doctor.get("doctorid").getAsInt()>0)
			return doctoresRepository.update(jsonDoctor);
		else
			return doctoresRepository.save(jsonDoctor);
	}
	
}

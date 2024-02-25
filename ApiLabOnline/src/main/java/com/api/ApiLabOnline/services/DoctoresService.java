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

	public JsonArray all() {
		return doctoresRepository.all();
	}

	public JsonArray list(int limit, int offset) {
		return doctoresRepository.list(limit, offset);
	}

	public JsonObject findById(Long tipoestudioid) {
		return doctoresRepository.findById(tipoestudioid);
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

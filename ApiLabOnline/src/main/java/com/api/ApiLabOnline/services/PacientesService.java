package com.api.ApiLabOnline.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.repository.PacientesRepository;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Service
public class PacientesService {

	@Autowired
	private PacientesRepository pacientesRepository;

	public JsonElement all() {
		return pacientesRepository.getAll();
	}

	public JsonElement list(int limit, int offset) {
		return pacientesRepository.list(limit, offset);
	}

	public JsonElement listByDoctorid(Long doctorid) {
		return pacientesRepository.listByDoctorid(doctorid);
	}

	public JsonObject findById(Long pacienteid) {
		return pacientesRepository.findById(pacienteid);
	}

	public JsonElement save(String jsonPaciente) {
		JsonObject paciente = new Gson().fromJson(jsonPaciente, JsonObject.class);
		if(paciente.get("pacienteid")!=null && paciente.get("pacienteid").getAsInt()>0) {
			return pacientesRepository.update(jsonPaciente);
		} else {
			return pacientesRepository.save(jsonPaciente);
		}
	}

}

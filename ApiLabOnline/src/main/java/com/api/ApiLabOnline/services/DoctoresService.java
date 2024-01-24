package com.api.ApiLabOnline.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.repository.DoctoresRepository;
import com.api.ApiLabOnline.repository.TipoestudiosRepository;
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

	public JsonObject save(String jsonTipoestudio) {
		return doctoresRepository.save(jsonTipoestudio);
	}
	
}

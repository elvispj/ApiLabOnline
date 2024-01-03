package com.api.ApiLabOnline.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.repository.TipoestudiosRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class TipoestudiosService {


	@Autowired
	private TipoestudiosRepository tipoestudiosRepository;

	public JsonArray all() {
		return tipoestudiosRepository.all();
	}

	public JsonArray list(int limit, int offset) {
		return tipoestudiosRepository.list(limit, offset);
	}

	public JsonObject findById(Long tipoestudioid) {
		return tipoestudiosRepository.findById(tipoestudioid);
	}

	public JsonObject save(String jsonTipoestudio) {
		return tipoestudiosRepository.save(jsonTipoestudio);
	}
}

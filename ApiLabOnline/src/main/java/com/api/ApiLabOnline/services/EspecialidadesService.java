package com.api.ApiLabOnline.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.repository.EspecialidadesRepository;
import com.google.gson.JsonArray;

@Service
public class EspecialidadesService {

	@Autowired
	private EspecialidadesRepository especialidadesRepository;
	
	public JsonArray listByDoctorid(Long doctorid) {
		return especialidadesRepository.listByDoctorid(doctorid);
	}
}

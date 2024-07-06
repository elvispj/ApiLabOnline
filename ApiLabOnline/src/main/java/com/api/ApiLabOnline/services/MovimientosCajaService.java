package com.api.ApiLabOnline.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.repository.MovimientosCajaRepository;
import com.google.gson.JsonObject;

@Service
public class MovimientosCajaService {

	@Autowired
	MovimientosCajaRepository movimientosCajaRepository;

	public JsonObject save(String movimientoscaja) {
		return movimientosCajaRepository.save(movimientoscaja);
	}
	
	public JsonObject findById(Long id) {
		return movimientosCajaRepository.findById(id);
	}

}

package com.api.ApiLabOnline.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.repository.FormasPagoRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class FormasPagoServices {

	@Autowired
	FormasPagoRepository formasPagoRepository;

	public JsonArray getAll() {
		return formasPagoRepository.getAll();
	}

	public JsonObject getById(String formapagoid) {
		return formasPagoRepository.getById(formapagoid);
	}

	public JsonObject saveOrUpdate(String formapago) {
		return formasPagoRepository.saveOrUpdate(formapago);
	}

	public void delete(Long formapagoid) {
		formasPagoRepository.delete(formapagoid);
	}

}

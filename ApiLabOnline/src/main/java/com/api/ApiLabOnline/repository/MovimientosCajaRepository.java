package com.api.ApiLabOnline.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface MovimientosCajaRepository {

	JsonArray getAll();

	JsonArray list(int limit, int offset);

	JsonObject findById(Long id);

	JsonObject update(String movimientocaja);

	JsonObject save(String movimientocaja);

	int deleteById(Long id);

}

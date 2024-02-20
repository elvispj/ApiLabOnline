package com.api.ApiLabOnline.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface TipoProductosRepository {

	JsonArray getAll();

	JsonArray list(int limit, int offset);

	JsonObject findById(Long id);

	void update(String tipoProductos);

	void save(String tipoProductos);

	int deleteById(Long id);

}

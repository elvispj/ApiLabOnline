package com.api.ApiLabOnline.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface InventarioRepository {

	JsonArray getAll();

	JsonArray list(int limit, int offset);

	JsonObject findById(Long id);

	void update(String inventario);

	void save(String inventario);

	int deleteById(Long id);

}

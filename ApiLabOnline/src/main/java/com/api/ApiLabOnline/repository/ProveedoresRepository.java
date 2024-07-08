package com.api.ApiLabOnline.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface ProveedoresRepository {

	JsonArray getAll();

	JsonArray list(int limit, int offset);

	JsonObject findById(Long id);

	JsonObject update(String proveedor);

	JsonObject save(String proveedor);

	int deleteById(Long id);

}

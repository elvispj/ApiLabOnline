package com.api.ApiLabOnline.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface ComprasRepository {

	JsonArray getAll();

	JsonArray getComprasProveedor();

	JsonArray list(int limit, int offset);

	JsonObject findById(Long id);

	JsonObject update(String compras);

	JsonObject save(String compras);

	int deleteById(Long id);

}

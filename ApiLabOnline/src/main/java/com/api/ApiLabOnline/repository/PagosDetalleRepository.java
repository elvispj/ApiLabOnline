package com.api.ApiLabOnline.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface PagosDetalleRepository {

	JsonArray getAll();

	JsonArray list(int limit, int offset);

	JsonArray findByPagoId(Long pagoid);

	JsonObject findById(Long id);

	void update(String compras);

	JsonObject save(String compras);

	int deleteById(Long id);

}

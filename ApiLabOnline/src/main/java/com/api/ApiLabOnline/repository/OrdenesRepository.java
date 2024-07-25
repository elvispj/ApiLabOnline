package com.api.ApiLabOnline.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface OrdenesRepository {

	JsonArray listOrdenes(int limit, int offset);

	JsonObject findById(Long id);

	JsonObject update(String jsonOrden);

	JsonObject save(String jsonOrden);

	int deleteById(Long id);

}

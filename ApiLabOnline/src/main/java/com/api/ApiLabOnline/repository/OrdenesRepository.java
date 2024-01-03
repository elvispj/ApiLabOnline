package com.api.ApiLabOnline.repository;

import java.util.Optional;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface OrdenesRepository {

	JsonArray listOrdenes(int limit, int offset);

	JsonObject findById(Long id);

	JsonObject save(String jsonOrden);

	int deleteById(Long id);

}

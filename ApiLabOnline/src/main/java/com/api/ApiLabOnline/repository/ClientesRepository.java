package com.api.ApiLabOnline.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface ClientesRepository {

	JsonArray all();

	JsonArray list(int limit, int offset);

	JsonObject findById(Long clienteid);

	JsonArray findByLike(String parametro);

	JsonObject update(String jsonCliente);

	JsonObject save(String jsonCliente);

}

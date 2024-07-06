package com.api.ApiLabOnline.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface PagosRepository {

	JsonArray getAll();

	JsonArray list(int limit, int offset);

	JsonObject findById(Long id);

	JsonObject update(String pago);

	JsonObject save(String pago);

	int deleteById(Long id);

	JsonObject getPagoByOrdenId(Long ordenid);

}

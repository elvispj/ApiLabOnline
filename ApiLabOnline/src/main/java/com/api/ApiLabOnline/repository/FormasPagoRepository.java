package com.api.ApiLabOnline.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface FormasPagoRepository {

	JsonArray getAll();

	JsonObject getById(String formapagoid);

	JsonObject saveOrUpdate(String jsonTipoestudio);

	void delete(Long tipoproductoid);

}

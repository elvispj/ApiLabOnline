package com.api.ApiLabOnline.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface PermisosRepository {

	JsonArray all();
	
	JsonObject findByUsuaioId(Long usuarioid);

	JsonObject findById(Long permisoid);

	JsonObject save(String jsonPermiso);

}

package com.api.ApiLabOnline.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface TipoestudiosRepository {

	JsonArray all();

	JsonArray list(int limit, int offset);

	JsonObject findById(Long tipoestudioid);

	JsonObject save(String jsonTipoestudio);

}

package com.api.ApiLabOnline.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface DoctoresRepository {

	JsonArray all();

	JsonArray list(int limit, int offset);

	JsonObject findById(Long tipoestudioid);

	JsonObject findByUsuarioId(Long usuarioid);

	JsonObject update(String jsonDoctor);

	JsonObject save(String jsonTipoestudio);
}

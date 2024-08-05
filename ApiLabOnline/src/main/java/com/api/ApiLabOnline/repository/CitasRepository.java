package com.api.ApiLabOnline.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface CitasRepository {

	JsonArray all();

	JsonArray list(int limit, int offset);

	JsonArray listByDoctorid(Long doctorid);

	JsonArray listByClienteid(Long clienteid);

	JsonObject findById(Long citaid);

	JsonObject update(String jsonCita);

	JsonObject save(String jsonCita);
}

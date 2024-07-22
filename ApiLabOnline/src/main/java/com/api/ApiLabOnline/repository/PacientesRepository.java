package com.api.ApiLabOnline.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface PacientesRepository {

	JsonArray getAll();

	JsonArray list(int limit, int offset);

	JsonArray listByDoctorid(Long doctorid);

	JsonObject findById(Long pacienteid);

	JsonObject save(String jsonPaciente);

}

package com.api.ApiLabOnline.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface EstudiosRepository {

	JsonArray getAll();

	JsonArray listEstudios(int limit, int offset);

	JsonObject findById(Long id);

	JsonObject save(String student);

	JsonObject update(String estudio);

	int deleteById(Long id);

}

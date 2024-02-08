package com.api.ApiLabOnline.repository;

import java.util.Optional;

import com.api.ApiLabOnline.entity.Estudios;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface EstudiosRepository {

	JsonArray getAll();

	JsonArray listEstudios(int limit, int offset);

	JsonObject findById(Long id);

	void save(String student);

	int deleteById(Long id);

}

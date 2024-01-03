package com.api.ApiLabOnline.repository;

import java.util.Optional;

import com.api.ApiLabOnline.entity.Estudios;
import com.google.gson.JsonArray;

public interface EstudiosRepository {

	JsonArray getAll();

	JsonArray listEstudios(int limit, int offset);

	Optional<Estudios> findById(Long id);

	void save(Estudios student);

	int deleteById(Long id);

}

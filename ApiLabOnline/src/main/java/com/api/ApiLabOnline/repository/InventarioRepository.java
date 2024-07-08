package com.api.ApiLabOnline.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface InventarioRepository {

	JsonArray getAll();

	JsonArray getAllTipoProducto();

	JsonArray list(int limit, int offset);

	JsonObject findById(Long id);

	JsonObject update(String inventario);

	JsonObject save(String inventario);

	int deleteById(Long id);

}

package com.api.ApiLabOnline.repository;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

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

	String SaveImage(MultipartFile multipartFile, Path rootLocation, JsonObject inventario);

	Resource getImageInventario(String filename, Path rootLocation);

}

package com.api.ApiLabOnline.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface MensajesRepository {

	JsonArray getMensajetipos();

	JsonArray listByDoctorid(Long doctorid);

	JsonArray listByClienteid(Long clienteid);

	JsonObject findById(Long mensajeid);

	JsonObject update(String jsonMensaje);

	JsonObject save(String jsonMensaje);
}

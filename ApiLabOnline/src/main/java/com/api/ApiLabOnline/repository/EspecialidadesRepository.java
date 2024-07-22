package com.api.ApiLabOnline.repository;

import com.google.gson.JsonArray;

public interface EspecialidadesRepository {

	JsonArray listByDoctorid(Long doctorid);

}

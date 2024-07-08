package com.api.ApiLabOnline.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.repository.EstudiosRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class EstudiosServices {
	@Autowired
	EstudiosRepository studentRepository;

	public JsonArray getAll() {
		return studentRepository.getAll();
	}
	
	public JsonArray listEstudios(int limit, int offset){
		return studentRepository.listEstudios(limit, offset);
	}
	
	public JsonObject getEstudio(Long id) {
		return studentRepository.findById(id);
	}
	
	public JsonObject saveOrUpdate(String estudios) {
		JsonObject estudio = new Gson().fromJson(estudios, JsonObject.class);
		if(estudio.has("estudioid") && estudio.get("estudioid")!=null 
				&& estudio.get("estudioid").getAsInt()>0)
			return studentRepository.update(estudios);
		else
			return studentRepository.save(estudios);
		
	}
	
	public int delete(Long id) {
		return studentRepository.deleteById(id);
	}

}

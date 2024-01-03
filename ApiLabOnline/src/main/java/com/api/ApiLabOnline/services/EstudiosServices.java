package com.api.ApiLabOnline.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.entity.Estudios;
import com.api.ApiLabOnline.repository.EstudiosRepository;
import com.google.gson.JsonArray;

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
	
	public Optional<Estudios> getEstudio(Long id) {
		return studentRepository.findById(id);
	}
	
	public void saveOrUpdate(Estudios estudios) {
		studentRepository.save(estudios);
	}
	
	public int delete(Long id) {
		return studentRepository.deleteById(id);
	}

}

package com.api.ApiLabOnline.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.ApiLabOnline.services.StorageServices;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("media")
@AllArgsConstructor
public class MediaController {
	@Autowired
	private StorageServices storageServices;
	@Autowired
	private HttpServletRequest request;
	
	@PostMapping("upload")
	public Map<String, String> uploadfile(@RequestParam("file") MultipartFile multipartFile){
		String path = storageServices.storage(multipartFile);
		String host = request.getRequestURL().toString().replace(request.getRequestURI(), "");
		String url = ServletUriComponentsBuilder
				.fromHttpUrl(host)
				.path("/media/")
				.path(path)
				.toUriString();
		Map<String, String> map = new HashMap<String, String>();
		map.put("url", url);
		return map;
	}
	
	@GetMapping("{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) throws IOException{
		Resource file = storageServices.loadAsResource(filename);
		String contentType = Files.probeContentType(file.getFile().toPath());
		
		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_TYPE, contentType)
				.body(file);		
	}
}

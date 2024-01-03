package com.api.ApiLabOnline.services;


import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageServices {
	
	void init() throws IOException;
	
	String storage(MultipartFile file);

	Resource loadAsResource(String filaname);
}

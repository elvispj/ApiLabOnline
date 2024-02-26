package com.api.ApiLabOnline.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.ApiLabOnline.services.ClientesService;
import com.google.gson.Gson;

@RestController
@RequestMapping(path="api/v1/clientes")
public class ClientesController {
	@Autowired
	private ClientesService clientesService;
	
	private Logger log = LogManager.getLogger(ClientesController.class);
	
	@GetMapping("/all/")
	public String getAll(){
		log.info("get all");
		return clientesService.all().toString();
	}
	
	@GetMapping("/list/")
	public String getList(@RequestParam("limit") int limit,@RequestParam("offset") int offset){
		log.info("Si llego all limit["+limit+"] offset["+offset+"]");
		return clientesService.list(limit, offset).toString();
	}
	
	@GetMapping("/search/{clienteid}")
	public String getById(@PathVariable("clienteid") Long clienteid){
		log.info("Search ById "+clienteid);
		return new Gson().toJson(clientesService.findById(clienteid));
	}
	
	@GetMapping("/searchByLike/{parametro}")
	public String getByLike(@PathVariable("parametro") String parametro){
		log.info("Search ByLike "+parametro);
		return clientesService.findByLike(parametro).toString();
	}
	
	@PostMapping("/save")
	public String saveUpdate(@RequestBody String jsonCliente){
		log.info("Save "+jsonCliente);
		return new Gson().toJson(clientesService.save(jsonCliente));
	}

}

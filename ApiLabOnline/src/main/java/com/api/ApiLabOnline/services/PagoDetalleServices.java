package com.api.ApiLabOnline.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.repository.PagosDetalleRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class PagoDetalleServices {

	@Autowired
	PagosDetalleRepository pagodetalleRepository;
	@Autowired
	MovimientosCajaService movimientosCajaService;

	public JsonArray findByPagoId(long pagoid) {
		JsonArray list = pagodetalleRepository.findByPagoId(pagoid);
		for(int i=0; i<list.size(); i++) {
			JsonObject pagoDetalle= list.get(i).getAsJsonObject();
			pagoDetalle.add("movimientoscaja", movimientosCajaService.findById(pagoDetalle.get("movimientocajaid").getAsLong()));
		}
		return list;
	}

	public JsonObject save(String detallepago) {
		return pagodetalleRepository.save(detallepago);
	}
}

package com.api.ApiLabOnline.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.repository.MovimientosCajaRepository;
import com.api.ApiLabOnline.repository.PagosDetalleRepository;
import com.api.ApiLabOnline.repository.PagosRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class PagosServices {
	
	@Autowired
	PagosRepository pagosRepository;
	@Autowired
	PagosDetalleRepository pagodetalleRepository;
	@Autowired
	MovimientosCajaRepository movimientosCajaRepository;

	public JsonArray getAll() {
		return pagosRepository.getAll();
	}
	
	public JsonArray list(int limit, int offset){
		return pagosRepository.list(limit, offset);
	}
	
	public JsonObject getById(Long id) {
		return pagosRepository.findById(id);
	}
	
	public void saveOrUpdate(String pago) {
		JsonObject jsonPagos = new Gson().fromJson(pago, JsonObject.class);
		if(jsonPagos.has("pagoid") && jsonPagos.get("pagoid")!=null 
				&& jsonPagos.get("pagoid").getAsInt()>0) {
			jsonPagos=pagosRepository.update(pago);
		} else {
			JsonArray listaPagosDetalle = jsonPagos.get("pagodetalle").getAsJsonArray();
			if(listaPagosDetalle!=null && listaPagosDetalle.size()>0) {
				jsonPagos=pagosRepository.save(pago);
				for(int i=0; i<listaPagosDetalle.size(); i++) {
					JsonObject detallepago = listaPagosDetalle.get(i).getAsJsonObject();
					System.out.println(">>> "+detallepago.get("movimientoscaja"));
					JsonObject movimientoscaja = movimientosCajaRepository.save(new Gson().toJson(detallepago.get("movimientoscaja")));
					movimientoscaja.addProperty("movimientocomentarios", "Se agrega pago por orden de estudio ID-"+jsonPagos.get("ordenid").getAsInt());
					detallepago.addProperty("pagoid", jsonPagos.get("pagoid").getAsInt());
					detallepago.addProperty("movimientocajaid", movimientoscaja.get("movimientoid").getAsInt());
					pagodetalleRepository.save(new Gson().toJson(detallepago));
				}
			}
		}
	}
	
	public int delete(Long id) {
		return pagosRepository.deleteById(id);
	}
}

package com.api.ApiLabOnline.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.repository.PagosRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class PagosServices {
	
	@Autowired
	PagosRepository pagosRepository;
	@Autowired
	MovimientosCajaService movimientosCajaService;
	@Autowired
	PagoDetalleServices pagoDetalleServices;

	public JsonArray getAll() {
		return pagosRepository.getAll();
	}
	
	public JsonArray list(int limit, int offset){
		return pagosRepository.list(limit, offset);
	}
	
	public JsonObject getById(Long id) {
		return pagosRepository.findById(id);
	}

	public JsonObject getPagoByOrdenId(Long ordenid) {
		JsonObject pago = pagosRepository.getPagoByOrdenId(ordenid);
		if(pago!=null)
			pago.add("pagodetalle", pagoDetalleServices.findByPagoId(pago.get("pagoid").getAsLong()));
		return pago;
	}
	
	public JsonObject saveOrUpdate(String pago) {
		JsonObject jsonPagos = new Gson().fromJson(pago, JsonObject.class);
		JsonArray listaPagosDetalle = jsonPagos.get("pagodetalle").getAsJsonArray();
		if(listaPagosDetalle!=null && listaPagosDetalle.size()>0) {
			if(jsonPagos.has("pagoid") && jsonPagos.get("pagoid")!=null 
					&& jsonPagos.get("pagoid").getAsInt()>0) {
				jsonPagos=pagosRepository.update(pago);
				
			} else {
				jsonPagos=pagosRepository.save(pago);
			}
			for(int i=0; i<listaPagosDetalle.size(); i++) {
				JsonObject detallepago = listaPagosDetalle.get(i).getAsJsonObject();
//				System.out.println(">>> "+detallepago.get("movimientoscaja"));
				JsonObject movimientoscaja = movimientosCajaService.save(new Gson().toJson(detallepago.get("movimientoscaja")));
				detallepago.addProperty("pagoid", jsonPagos.get("pagoid").getAsInt());
				detallepago.addProperty("movimientocajaid", movimientoscaja.get("movimientoid").getAsInt());
				detallepago = pagoDetalleServices.save(new Gson().toJson(detallepago));
			}
			return jsonPagos;
		} else {
			return null;
		}
	}
	
	public int delete(Long id) {
		return pagosRepository.deleteById(id);
	}
}

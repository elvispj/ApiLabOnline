package com.api.ApiLabOnline.entity;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Perfil {
	int perfilid;
	boolean perfilactivo;
	String perfilnombre;
	String perfildescripcion;
	Timestamp perfilfechacreacion;
	Timestamp perfilfechamodificacion;
}

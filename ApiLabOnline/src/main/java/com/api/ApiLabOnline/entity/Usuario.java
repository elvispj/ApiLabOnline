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
public class Usuario {
	 int usuarioid;
	 int perfilid;
	 int colaboradorid;
	 boolean usuarioactivo;
	 String usuariocorreo;
	 String usuariopwd;
	 String usuarionombre;
	 String usuarioapellidopaterno;
	 String usuarioapellidomaterno;
	 Timestamp usuariofechacreacion;
	 Timestamp usuariofechamodificacion;
	 Timestamp usuarioultimoacceso;
	 String usuariokey;
	 String usuarioimage;
	 String token;
}

package com.api.ApiLabOnline.utils;

import java.text.SimpleDateFormat;

public class Utils {

	public static String getFechaActual() {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
		return formato.format(System.currentTimeMillis());
	}

}

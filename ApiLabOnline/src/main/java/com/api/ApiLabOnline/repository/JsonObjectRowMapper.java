package com.api.ApiLabOnline.repository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.google.gson.JsonObject;

public class JsonObjectRowMapper implements RowMapper<JsonObject> {

    public JsonObjectRowMapper() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public JsonObject mapRow(ResultSet rs, int rowNum) throws SQLException {
    	JsonObject columns = new JsonObject();
        ResultSetMetaData rsMeta = rs.getMetaData();
        int colCount = rsMeta.getColumnCount();

        for (int i = 1; i <= colCount; i++) {
        	columns.addProperty(rsMeta.getColumnLabel(i), (rs.getObject(i)!=null ? rs.getObject(i).toString() : ""));
        }
        return columns;
    }
}

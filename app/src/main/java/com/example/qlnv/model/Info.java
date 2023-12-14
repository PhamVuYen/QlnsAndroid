package com.example.qlnv.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


import java.io.Serializable;
public class Info implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	public Info(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public Info() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.id +" - "+this.name;
	}
}

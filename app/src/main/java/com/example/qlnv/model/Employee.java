package com.example.qlnv.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;


@Entity(tableName = "user")
public class Employee  implements Serializable{
	@PrimaryKey(autoGenerate = false)
	@NotNull
	private String id;
	private String name;
	private Date dateOfbirth;
	private String address;
	private boolean sex;
	private String phone;
	private String email;
	private String identified;
	private String role;
	private String idRoom;
	private String stk;
	private String mucluong;
	private String password;

	public Employee(@NotNull String id, String name, Date dateOfbirth, String address, boolean sex, String phone, String email, String identified, String role, String idRoom, String stk, String mucluong) {
		this.id = id;
		this.name = name;
		this.dateOfbirth = dateOfbirth;
		this.address = address;
		this.sex = sex;
		this.phone = phone;
		this.email = email;
		this.identified = identified;
		this.role = role;
		this.idRoom = idRoom;
		this.stk = stk;
		this.mucluong = mucluong;
	}

//	public Employee(@NotNull String id, String name, Date dateOfbirth, String address, boolean sex, String phone, String email, String identified, String role, String idRoom, String stk, String mucluong) {
//		this.id = id;
//		this.name = name;
//		this.dateOfbirth = dateOfbirth;
//		this.address = address;
//		this.sex = sex;
//		this.phone = phone;
//		this.email = email;
//		this.identified = identified;
//		this.role = role;
//		this.idRoom = idRoom;
//		this.stk = stk;
//		this.mucluong = mucluong;
//	}

	public Employee() {
	}

	public String getStk() {
		return stk;
	}

	public void setStk(String stk) {
		this.stk = stk;
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

	public Date getDateOfbirth() {
		return dateOfbirth;
	}

	public void setDateOfbirth(Date dateOfbirth) {
		this.dateOfbirth = dateOfbirth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdentified() {
		return identified;
	}

	public void setIdentified(String identified) {
		this.identified = identified;
	}

	public String getIdRoom() {
		return idRoom;
	}

	public void setIdRoom(String idRoom) {
		this.idRoom = idRoom;
	}

	public boolean isSex() {
		return sex;
	}
	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRoom() {
		return idRoom;
	}
	public void setRoom(String idRoom) {
		this.idRoom = idRoom;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMucluong() {
		return mucluong;
	}

	public void setMucluong(String mucluong) {
		this.mucluong = mucluong;
	}

	@Override
	public String toString() {
		return name + " - " + mucluong;
	}
}

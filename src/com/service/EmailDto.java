package com.service;

public class EmailDto
{
	private int id;
	private int rid;//rid
	private String con;//content
	private int sdt;//sendtime
	private int st;//status
	private int type;  // 1.被焚烧  2.jjc被挑战 3.魔灯结算
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCon() {
		return con;
	}
	public void setCon(String con) {
		this.con = con;
	}
	public int getSdt() {
		return sdt;
	}
	public void setSdt(int sdt) {
		this.sdt = sdt;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public int getRid() {
		return rid;
	}
	public void setSt(int st) {
		this.st = st;
	}
	public int getSt() {
		return st;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}
}
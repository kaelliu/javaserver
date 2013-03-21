package com.data;

//import java.util.Map;
//import org.jboss.netty.util.internal.ConcurrentHashMap;

public class SessionData {


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNm() {
		return nm;
	}
	public void setNm(String nm) {
		this.nm = nm;
	}
	private int id;
//	public short curKey = 1;
//	public Map<Short,Short> keys = new ConcurrentHashMap<Short,Short>();
//	public Map<Short,Long> calls = new ConcurrentHashMap<Short,Long>();
	public String nm;
	
}

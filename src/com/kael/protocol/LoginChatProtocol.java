package com.kael.protocol;

import lib.kael.ProtocolBase;

public class LoginChatProtocol extends ProtocolBase{
	private String nm;
	private int    id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LoginChatProtocol()
	{
		this.Cmd = ProtocolMatch.MAIN_CMD_LOGIN_LOGIN;
	}
	public String getNm() {
		return nm;
	}

	public void setNm(String nm) {
		this.nm = nm;
	}

}

package com.kael.protocol;

import lib.kael.ProtocolBase;

public class ChatBroadCastProtocol extends ProtocolBase{
	private String msg;
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ChatBroadCastProtocol()
	{
		this.Cmd = ProtocolMatch.MAIN_CMD_SEND_CHAT_MSG;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}

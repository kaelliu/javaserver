package com.kael.protocol;

import lib.kael.ProtocolBase;

public class ChatProtocol extends ProtocolBase{
	private String msg;
	public ChatProtocol()
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

package com.kael.protocol;

import java.util.Map;

import lib.kael.ProtocolBase;

public class UpdateUserInfoProtocol extends ProtocolBase{
	private Map d;
	public Map getD() {
		return d;
	}
	public void setD(Map d) {
		this.d = d;
	}
	public UpdateUserInfoProtocol()
	{
		this.Cmd = ProtocolMatch.PS_CMD_MODIFY;
	}
}

package com.kael.protocol;

import lib.kael.ProtocolBase;

public class QueryUserProtocol extends ProtocolBase{
	private int uid;
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public QueryUserProtocol()
	{
		this.Cmd = ProtocolMatch.PS_CMD_QUERY;
	}
}

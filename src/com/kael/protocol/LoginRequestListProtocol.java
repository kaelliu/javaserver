package com.kael.protocol;

import java.util.ArrayList;
import java.util.List;

import lib.kael.ProtocolBase;

import com.data.SessionData;

public class LoginRequestListProtocol extends ProtocolBase{
	public List<SessionData> getUsers() {
		return users;
	}

	public void setUsers(List<SessionData> users) {
		this.users = users;
	}

	private List<SessionData> users = new ArrayList<SessionData>();
	
	public LoginRequestListProtocol()
	{
		this.Cmd = ProtocolMatch.MAIN_CMD_USER_LIST;
	}
}

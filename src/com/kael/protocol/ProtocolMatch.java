package com.kael.protocol;

public class ProtocolMatch
{
	// s->c
	// no data send ,just tell client it can login
	public final static short MAIN_CMD_CONNECTED   = 2989;
	// c->s
	// data send:username,password,bla,chat room login
	// broad cast to other,some one login
	public final static short MAIN_CMD_LOGIN_LOGIN = 1;
	// s->c
	// data send back:operate command id
	public final static short MAIN_CMD_OPERATE_RESULT  = 1000;
	public final static short MAIN_CMD_OPERATE_SUCCESS = 1001;
	// data send back:error code
	public final static short MAIN_CMD_OPERATE_FAILED  = 1002;
	// c->s
	// data send:chat content sended
	// s->c
	// data send:broadcast chat data
	public final static short MAIN_CMD_SEND_CHAT_MSG = 2;
	
	// s->c
	// data send:uid,user gone,broadcast
	public final static short MAIN_CMD_USER_GONE = 3;
	
	public final static short MAIN_CMD_USER_LIST = 4;

}

package com.kael.protocol;

public class ErrorCode
{
	public static short ERROR_OK					     = 0;
	public static short ERROR_LOGIN_REGISTER_SAME_NAME   = 1;// 已存在同名用户
	public static short ERROR_LOGIN_FAILED   			 = 2;// 登陆失败
	public static short ERROR_REGISTE_FAILED   			 = 3;// 注册失败
}

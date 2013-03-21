package com.kael;

import lib.kael.LogicHandler;
import lib.kael.PojoMapper;
import lib.kael.ServerApp;

import org.codehaus.jackson.JsonNode;
import org.jboss.netty.channel.Channel;
import com.kael.protocol.ProtocolMatch;
import com.toplogic.ChatLogic;

public class GameServerLogicHandler implements LogicHandler
{
	@Override
	public boolean handle(Object data, Channel ch) {
		// get command first
		String hash = (String)data;
		if(hash.length() == 0)
			return false;
		JsonNode rootNode = PojoMapper.toNode(hash);
		if(rootNode==null){
			System.out.println("JsonNode wrong!");
			return false;
		}
//		short command = (short) jo.getInt("cmd");System.out.println("cmd:"+command);
		int command = rootNode.get("cmd").getIntValue();
//		if(!rootNode.has("ke"))return false;
//		int k = rootNode.get("ke").getIntValue();
		
		try{
			switch(command)
			{
				// c -> s handler part
//				case ProtocolMatch.MAIN_CMD_LOGIN_REGISTE_TO_GAME:
//					return ((LoginLogic)getBean("loginLogic")).registerMD5(hash,ch);
//				case ProtocolMatch.MAIN_CMD_LOGIN_SUC_WITHMD5:
//					return ((LoginLogic)getBean("loginLogic")).handlegetLogin(hash, ch);
				case ProtocolMatch.MAIN_CMD_LOGIN_LOGIN:
					return ((ChatLogic)getBean("chatLogic")).handleRoleCome(hash,ch);
//					return ((ChatLogic)getBean("chatLogic")).handleRoleComeSendListBack(hash, ch);
				case ProtocolMatch.MAIN_CMD_SEND_CHAT_MSG:
					return ((ChatLogic)getBean("chatLogic")).handleChatInfo(hash,ch);
				default:
					return false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	private Object getBean(String name){
		return ServerApp.getInstance().m_context.getBean(name);
	}
}
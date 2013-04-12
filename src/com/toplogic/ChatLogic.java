package com.toplogic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.jboss.netty.channel.Channel;

import com.data.SessionData;
import com.kael.GameServer;
import com.kael.protocol.ChatBroadCastProtocol;
import com.kael.protocol.ChatProtocol;
import com.kael.protocol.ConnectedOrDisConnectProtocol;
import com.kael.protocol.LoginChatProtocol;
import com.kael.protocol.LoginProtocol;
import com.kael.protocol.LoginRequestListProtocol;
import com.kael.protocol.ProtocolMatch;

public class ChatLogic {
	
	public boolean handleRoleComeSendListBack(String msg,Channel ch)
	{
		LoginChatProtocol lp = (LoginChatProtocol) new LoginChatProtocol().fromJObj(msg);
		// 1.save to memory session data
		if(GameServer.sessionDatas.containsKey(ch))
		{
			return false;
		}
		SessionData sd = new SessionData();
		sd.setNm(lp.getNm());
		sd.setId(lp.getId());
		GameServer.sessionDatas.put(ch, sd);
		LoginRequestListProtocol lrlp = new LoginRequestListProtocol();
		List lst = new ArrayList<SessionData>();
		Set entrys = GameServer.sessionDatas.entrySet();
		{
			Iterator it = entrys.iterator();
			while(it.hasNext()) 
			{
				Entry entry = (Entry) it.next();
				Channel other_ch = (Channel) entry.getKey();
				other_ch.write(lp);
				SessionData sdother = (SessionData) entry.getValue();
				lst.add(sdother);
				if(other_ch == ch)// me
				{	
					continue;
				}
//				ch.write(toMe);
			}
		}
		ch.write(lrlp);
		return true;
	}
	
	public boolean handleRoleCome(String msg,Channel ch)
	{
		LoginChatProtocol lp = (LoginChatProtocol) new LoginChatProtocol().fromJObj(msg);
		// 1.save to memory session data
		if(GameServer.sessionDatas.containsKey(ch))
		{
			return false;
		}

		// login successful,send back other user list
		
		SessionData sd = new SessionData();
		sd.setNm(lp.getNm());
		sd.setId(lp.getId());
		GameServer.sessionDatas.put(ch, sd);
		//GameServer.idWithChannel.put(lp.getId(), ch);
		// 2.broadcast to other user
		Set entrys = GameServer.sessionDatas.entrySet();
		LoginChatProtocol toMe = new LoginChatProtocol();
		//if(entrys.size() > 1)// not only me
		{
			Iterator it = entrys.iterator();
			while(it.hasNext()) 
			{
				Entry entry = (Entry) it.next();
				Channel other_ch = (Channel) entry.getKey();
				other_ch.write(lp);
				if(other_ch == ch)// me
				{	
					continue;
				}
				SessionData sdother = (SessionData) entry.getValue();
				toMe.setId(sdother.getId());
				toMe.setNm(sdother.getNm());
				ch.write(toMe);
			}
		}
		System.out.println("user login over");
		return true;
	}
	
	public static void handleRoleGone(Channel ch)
	{
		// 1.remove from session data
		SessionData sd = GameServer.sessionDatas.get(ch);
		if(sd!=null)
		{
			ConnectedOrDisConnectProtocol ccp = new ConnectedOrDisConnectProtocol();
			ccp.setCmd(ProtocolMatch.MAIN_CMD_USER_GONE);
			ccp.setId(sd.getId());
			//GameServer.idWithChannel.remove(sd.getUid());
			GameServer.sessionDatas.remove(ch);
			// 2.broadcast to other user
			Set entrys = GameServer.sessionDatas.entrySet();
			//if(entrys.size() > 1)// not only me
			{
				Iterator it = entrys.iterator();
				while(it.hasNext()) 
				{
					Entry entry = (Entry) it.next();
					Channel other_ch = (Channel) entry.getKey();
					if(other_ch == ch)// me
						continue;
					other_ch.write(ccp);
				}
			}
		}
	}
	
	public boolean handleChatInfo(String msg,Channel ch)
	{
		// broad cast to other user
		SessionData sd = GameServer.sessionDatas.get(ch);
		ChatProtocol cp = (ChatProtocol) new ChatProtocol().fromJObj(msg);
		Set entrys = GameServer.sessionDatas.entrySet();
		ChatBroadCastProtocol cbcp = new ChatBroadCastProtocol();
		cbcp.setId(sd.getId());
		cbcp.setMsg(cp.getMsg());
		if(entrys.size() > 1)// not only me
		{
			Iterator it = entrys.iterator();
			while(it.hasNext()) 
			{
				Entry entry = (Entry) it.next();
				Channel other_ch = (Channel) entry.getKey();
				if(other_ch == ch)// me
					continue;
				other_ch.write(cbcp);
			}
		}
		System.out.println("user send message");
		return true;
	}
}

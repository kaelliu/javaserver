package com.kael;

import lib.kael.ConnectionHandler;
import lib.kael.ProtocolBase;
import lib.kael.ServerApp;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import com.kael.GameServer;
import com.kael.protocol.ChatProtocol;
import com.kael.protocol.ConnectedOrDisConnectProtocol;
import com.kael.protocol.ErrorCode;
import com.kael.protocol.OperateResultProtocol;
import com.kael.protocol.ProtocolMatch;
import com.toplogic.ChatLogic;

public class GameConnectionHandler implements ConnectionHandler
{

	@Override
	public boolean handleConnect(Channel ch) {
		//
//		if(GameServer.sessionDatas.size()+1 > GameServer.onlineLimit)
//		{
//			// oops...we control the online people,maybe you would try next time
////			ch.close();
//			// solution #1,tell the client,here have bla,bla user still waiting,and u r in queue now..
//			// if waiting queue is full also
//			synchronized(GameServer.waitingList) { 
//				if(GameServer.waitingList.size()+1 > GameServer.onlineHold)
//				{
//					OperateResultProtocol orp = new OperateResultProtocol(ErrorCode.ERROR_ONLINE_MAX, false);
//					ChannelFuture future = ch.write(orp);
//					future.addListener(new ChannelFutureListener() {
//			             public void operationComplete(ChannelFuture future) {
//			                 // Perform post-closure operation
//			                 future.getChannel().close();
//			             }
//			         });
//				}
//			}
			// solution #2,tell the client,you are off line directly
			// wait for write complete,we close the channel
			// tell him,u r offline
//			OperateResultProtocol orp = new OperateResultProtocol(ErrorCode.ERROR_ONLINE_MAX, false);
//			ChannelFuture future = ch.write(orp);
//			future.addListener(new ChannelFutureListener() {
//	             public void operationComplete(ChannelFuture future) {
//	                 // Perform post-closure operation
//	                 future.getChannel().close();
//	             }
//	         });
//		}
//		ProtocolBase pb = new ProtocolBase();
//		pb.setCmd(ProtocolMatch.MAIN_CMD_CONNECTED);
//		ch.write(pb);
		int id = GameServer.ids.getAndIncrement();
		ConnectedOrDisConnectProtocol ccp = new ConnectedOrDisConnectProtocol();
		ccp.setId(id);
		ch.write(ccp);
		System.out.println("user connected,id is:"+id);
		return true;
	}

	@Override
	public boolean handleDisConnect(Channel ch) {
		// 
		ChatLogic.handleRoleGone(ch);
		System.out.println("user disconnected");
//		GameServer.channelAmount=(short) (GameServer.channelAmount-1);
//		if(GameServer.usrChannel != null)
//		{
//			return true;
//		}
//		else
//			return false;
		return true;
	}

	private Object getBean(String name){
		return ServerApp.getInstance().m_context.getBean(name);
	}
}
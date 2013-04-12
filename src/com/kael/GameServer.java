package com.kael;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.channel.Channel;

import redis.clients.jedis.JedisPool;

import lib.kael.ServerApp;
import lib.kael.TcpNetworkEngine;
import com.data.SessionData;

public class GameServer
{
	public static int PORT=8888;
	public static Map keyMatch;
	public static Map usrChannel;// map<mapid,<channel_id,rid>> --map<mapid,map<channel,userconnection>>	//�õ�ͼ�������û�
	public static Map<Channel,SessionData> sessionDatas;		//�����û�
	public static Map<Channel,Integer> 	   sessionDatasCache;	//�����û�   -- concurrentHashMap for channel,redis user id
//	public static Map<Integer,Channel> idWithChannel;			//�û�����Ƶ��
	public static int RobotCount=0;
	public static final int RobotStart=2348;
	public static int onlineLimit = 2000;
	public static short onlineHold = 500;// ���ȴ�������
	public static short channelAmount = 0;// alive channel count
	public static int onlineMax=0;
	public static final int broadcastThreadSampleCount=100;//�����С
	public static Map<Integer,Vector<SessionData>> broadcastZone;//�㲥��,--> ��ID,sessionData
	public static ConcurrentLinkedQueue<Channel> waitingList;// �Ŷ��б�
	public static Map<Channel,String> waitingListLoginMsg;   // �Ŷ��б��½��Ϣ
	public static int POWEROONTIME = 0;
	public static AtomicInteger ids = new AtomicInteger();
	public static JedisPool cacheMgr;
	public GameServer()
	{
		ids.set(0);
		POWEROONTIME = (int) (System.currentTimeMillis()/1000);
		new TcpNetworkEngine(PORT);
		ServerApp.getInstance().set_logic(new GameServerLogicHandler());
		ServerApp.getInstance().set_conn(new GameConnectionHandler());

		keyMatch=new HashMap();
		ServerApp.getInstance();
		usrChannel = new ConcurrentHashMap<Integer,ConcurrentHashMap<Channel,Integer>>();
		sessionDatas = new ConcurrentHashMap<Channel,SessionData>();
		broadcastZone = new ConcurrentHashMap<Integer, Vector<SessionData>>();
//		sellBagDatas = new ArrayList<SellBagData>();
		waitingList = new ConcurrentLinkedQueue<Channel>();
		waitingListLoginMsg = new ConcurrentHashMap<Channel,String>();
		for(int i=0;i<40;++i)
		{
			Vector<SessionData> arr = new Vector<SessionData>();
			broadcastZone.put(i, arr);// make 40 constant,40*60=2400 zone user for now
		}
		cacheMgr = (JedisPool) ServerApp.getInstance().m_context.getBean("jedisPool");
	}
	public static void main(String []args)
	{
		try{
			new GameServer();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
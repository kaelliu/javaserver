package com.toplogic;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jboss.netty.channel.Channel;

import redis.clients.jedis.Jedis;

import com.kael.GameServer;
import com.service.UserService;

public class TimeTaskLogic {

	public static final byte WORLDMSG_DEF    = 1;
	public static final byte WORLDMSG_LAMP   = 2;
	public static final byte WORLDMSG_CHESS  = 3;
	public static final byte WORLDMSG_MANZU  = 4;
	public static final byte WORLDMSG_SIDE   = 5;
	public static final byte WORLDMSG_SWAR   = 6;
	public static final byte WORLDMSG_JTWAR  = 7;
	private UserService userService;
	long lastCurrentTime = System.currentTimeMillis();
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	private int cnt = 0;
	//////////////////////////////////////////////////////////////////////////////////////
	//  机动定时处理相关的对外接口，比如：刷副本。  
	//////////////////////////////////////////////////////////////////////////////////////
	/*每分执行*/
	public void handleByMinute(){
		Calendar c = Calendar.getInstance();
		cnt++;
		if(cnt > 2)
		{
			cnt = 0;
			recordInfo();
		}
	}
	
	private void recordInfo()
	{
		// 写入最新的分数
		Jedis jedis = GameServer.cacheMgr.getResource();
		long now = System.currentTimeMillis();//currentTime - 180000;
		Set<String> set = jedis.zrangeByScore("lastdirty", lastCurrentTime, now);
		Iterator<String> i = set.iterator();
		while(i.hasNext())
		{
			String id = i.next();
			int uid = Integer.parseInt(id);
			if(uid < 0)
				continue;
			// find what tobe update
			// INSERT into xxx(id,row,r) VALUES(1,234,444) on DUPLICATE KEY UPDATE row=234,r=444
			Set<String> dirty = jedis.smembers("u:"+id+":info_dirty");
			Map need = new HashMap();
			Iterator<String> i2 = set.iterator();
			while(i2.hasNext())
			{
				String column = i2.next();
//				Integer value = Integer.parseInt(jedis.hget("u:"+id, column));
				// if is a number/int/byte/or something? this need fix
				need.put(column, jedis.hget("u:"+id, column));
			}
			userService.changeUser(need);
		}
		// clear the sorted set
		jedis.zremrangeByScore("lastdirty", lastCurrentTime, now);
		lastCurrentTime = System.currentTimeMillis();
	}
	
	
	public void removeHangUpTask(String msg,Channel ch){
	}
	//重启 扫荡
	public void restartHangUpTask(String msg,Channel ch){
	}
	//扫荡加速
	public void speedHangUpFight(String msg,Channel ch){
	}
	
	//英雄本扫荡
	public void hangUpByHero(String msg,Channel ch)
	{
		
	}
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////
	//  批处理的时间点方法入口部分   
	//////////////////////////////////////////////////////////////////////////////////////
	

	/*每天19:00,19:10,19:20,19:30 发送报名消息*/
	public void handleSociatyNotice1(){

	}
	/*每天19:05,19:15,19:25,19:35 发送战斗消息*/
	public void handleSociatyNotice2(){
	}
	/*每个整点时执行*/
	public void handleByHour(){
		//排序公会排名
		//
		/**
		 * 封测用：：：
		 * 
		 * 10.00 -- 10.30 公会防御战
		 * 13.30 -- 14.00 灯
		 * 14.30 -- 15.00 暗棋
		 * 15.30 -- 16.00 蛮族
		 * 18.00 -- 18.30 阵营
		 * 21.00 -- 21.30 军团战
		 * 22.00  竞技场奖励结算
		 */
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if(hour==0){//
			handleBy_24_00();//系统的24点批处理
		}else if(hour==1){//
			//清理一下 飞鸽中的死鸽
		}else if(hour==2){//
			//清理聊天记录的超期记录
		/* 回购代码屏蔽处	如需开启取消注释
			clearSellGood();
		*/
		}else if(hour==10){
		}else if(hour==11){
		}else if(hour==12){
			handleBy_12_00();//系统的24点批处理
		}else if(hour==13){
		}else if(hour==14){
		}else if(hour==15){
		}else if(hour==16){
		}else if(hour==17){
		}else if(hour==18){
			daliy_side_war();//阵营战开启
//			WorldMessageLogic.worldMessage(WORLDMSG_SIDE);
		}else if(hour==19){
		}else if(hour == 21){
			
		}else if(hour == 20){
		}
		else if(hour == 22){	
		}
		
		// generateLampWorldBoss();//生成护灯魔神   clearLampBoss();//清理护灯魔神
		// daliy_side_war();//阵营战开启      daliy_side_war_end();//阵营战结束
		// alarmDarkChessStart();//暗棋活动开始      alermDarkChessEnd();//暗棋战结束
		// daliy_world_boss();//生成南蛮的战场      daliy_world_boss_end();//蛮族入侵结束，处理奖励
		// daliy_defend_war();//公会防御战开始    daliy_defend_war_end();//公会防御战结束
		// initSociatyDatas();//初始化公会战初始信息。clearSociatyWarDatas();//清理公会战信息
	}
	/* 每个 n点30分的时候执行 */
	public void handleByThirty(){
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if(hour==10){
		}else if(hour==13){
		}else if(hour==14){
		}else if(hour==15){
		}else if(hour==17){
		}else if(hour==18){
			////////////////////////////
		}else if(hour == 20 )
		{
		}else if(hour==22){
		}
	}

	
	/*每秒执行*/
	public void handleBySecond(){
	}

	//////////////////////////////////////////////////////////////////////////////////////
	//  管理后台特殊操作。
	//////////////////////////////////////////////////////////////////////////////////////
	//关服处理 
	public boolean beforeServerClosed(){
		//同步坐标和地图。
		return true;
	}
	public boolean startOneAction(String str){//开启某个活动。

		return true;
	}
	public boolean runBatchChanging(){
		handleBy_24_00();
		return true;
	}
	//////////////////////////////////////////////////////////////////////////////////////
	//  批处理调用的各私有方法，内部处理部分
	//////////////////////////////////////////////////////////////////////////////////////
	// -------------   普通的     ----------
	private void handleBy_24_00(){
//		//3.遁甲天书
//		doQuestion();
		//7.重置所有角色各消耗限制性数据
		recoverRole(1);
		//4.特殊副本重置
		//5.设置军团城市成员
		//6.重置每天的竞技次数
		//1、VIP
		//2、公会
		//8.日常任务
		//9.历练领取
		updateAllSessionDatas_2400(1);
	}
	private void handleBy_12_00(){
		recoverRole(2);
		updateAllSessionDatas_2400(2);
	}
	
	private void updateAllSessionDatas_2400(int range)
	{
		
	}

	private void recordOnLineInfo() {
//		Map param = new HashMap();
//		param.put("ts", System.currentTimeMillis() / 1000);
//		param.put("oc", GameServer.sessionDatas.size());
//		param.put("pc", GameServer.onlineMax);
//		GameServer.onlineMax = GameServer.sessionDatas.size();
//		onlineDataService.createOnlineData(param);
	}
	
	// 阵营战开启，每天晚上8点
	private void daliy_side_war()
	{
		// 通知报名开启
		doAt_20_10();
	}
	
	// 阵营战结束，每天晚上8点30
	private void daliy_side_war_end()
	{
		doAt_20_40();
		// 通知结束
		// 统计战况胜负
		// 给胜利的奖励，给参战的人发奖励。如果掉线的人也要发通知？
		// 清除临时数据
	}
	
	/** 批处理角色的数据  回复各种次数等  */
	private void recoverRole(int range) {
		/*birdNum = 5    可免费放飞次数
		callNum = 5    可免费召唤次数
		curBirdNum = 0 当前放飞次数
		curCallNum = 0 当前召唤次数
		ffNum = 3  放灯次数
		fsNum = 3  焚烧次数
		hsNum = 3  护送次数
		salaryNum = 1  领俸禄次数
		answerNum =0  已答题次数
		rmbLampNum = 0  刷灯次数
		zsNum = 6    可免费征税次数
		curFsNum = 0 当前焚烧次数
		curFfNum = 0 当前放灯次数*/
		Map rm = new HashMap();
		if(range==1){
			rm.put("bn", 5);
			rm.put("cln", 5);
			rm.put("cbn", 0);
			rm.put("ccn", 0);
			rm.put("sun", 1);
			rm.put("an", 0);
			rm.put("rln", 0);
			rm.put("zn", 6);
			rm.put("cfn", 0);
			rm.put("csn", 0);
			rm.put("cn", 3);
			rm.put("olt", 0);// 当日在线时间清空
			rm.put("eds", 0);
			rm.put("slp", "0,0,0,0,0,0,0,0,0");
			rm.put("sln", 0);
			rm.put("rsln", 0);
		}else if(range==2){
			rm.put("fn", 3);
			rm.put("sn", 3);
			rm.put("hsn", 3);
		}
		rm = null;
	}
	
	private void changeMap(Map map, int lv){
		switch(lv)
		{
			case 1: //50000铜钱	
				map.put("money", 50000);
				break;		
			case 2: //20粮草
				map.put("ceng", 20);
				break;		
			case 3://TODO 蓝色材料箱（发送礼包3ID）
				break;		
			case 4: //20元宝
				map.put("rmb", 20);
				break;
			case 5: //TODO 中级卡册（发送礼包5ID）
				break;
			default: break;
		}
	}
	
	//20:10分初始化阵营战信息
	private void doAt_20_10(){
	}

	//20:40分阵营战结束
	private void doAt_20_40(){
		
	}
}

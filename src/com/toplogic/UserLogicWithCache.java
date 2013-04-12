package com.toplogic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jboss.netty.channel.Channel;

import redis.clients.jedis.Jedis;

import com.kael.GameServer;
import com.kael.protocol.DeleteUserProtocol;
import com.kael.protocol.ErrorCode;
import com.kael.protocol.LoginProtocol;
import com.kael.protocol.OperateResultProtocol;
import com.kael.protocol.QueryUserProtocol;
import com.kael.protocol.RegisterLoginSuccessProtocol;
import com.kael.protocol.RegisterProtocol;
import com.kael.protocol.UpdateUserInfoProtocol;
import com.service.UserDetailDto;
import com.service.UserService;

/*
 * for cache system test usage
 * all user run-time data save to redis instead of origin save in server's memory
 * dirty data will write back for each 3 minutes,write last 3 minutes unsaved 
 * staff into database,with different ,dirtyed column
 * the structure is like this
 * u:id  					- un/sex/pass/ico/nm/ 		hash
 * u:id:info:dirty_c 		- dirty column     		    set 
 * lastdirtytime			- score(time) value(uid)    sorted set
 * map:id:users			    - id,id,id					list
 * channel is relate with the only one uid
 */

/*
 * solution b,use protobuf as value,not hash
 * u:id  					- object detail byte array 	string
 * u:id:info:dirty_c 		- dirty column     		    set 
 * lastdirtytime			- score(time) value(uid)    sorted set
 * map:id:users			    - id,id,id					list
 * */
public class UserLogicWithCache {
	private UserService userService;
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public boolean handleQueryUser(String msg, Channel ch) {
		QueryUserProtocol rp = (QueryUserProtocol) new QueryUserProtocol().fromJObj(msg);
		UserDetailDto ud = getUserDetail(rp.getUid());
		return true;
	}
	
	private UserDetailDto getUserDetail(int uid)
	{
		Jedis jedis = GameServer.cacheMgr.getResource();
		String key = "u:"+uid;
		UserDetailDto ud;
		// is it already in cache? -- other one query user,will save to cache?
		if(jedis.hkeys(key).size() > 0)
		{
			// already in cache
			ud = new UserDetailDto();
			ud.setUid(uid);
			ud.setNm(jedis.hget(key, "nm"));
			ud.setSex((byte) Integer.parseInt(jedis.hget(key, "sex")));
			ud.setIcon((byte) Integer.parseInt(jedis.hget(key, "ico")));
		}
		else
		{
			// get the detail info here
			ud = userService.getuserDetail(uid);
			if(ud == null);
			else
			{
				// first time cache initial		
				jedis.hset(key, "sex", String.valueOf(ud.getSex()));
				jedis.hset(key, "nm", ud.getNm());
				jedis.hset(key, "ico",  String.valueOf(ud.getIcon()));
				// delete
//				jedis.expire(key, 0);
			}
		}
		return ud;
	}
	
	public boolean handleGetItem(String msg, Channel ch) {
		int iid = 911;
		Jedis jedis = GameServer.cacheMgr.getResource();
		String key = "u:1:items";
		String key2 = "u:1:item:id"+iid;
		jedis.lpush(key, String.valueOf(iid));
		jedis.hset(key2, "addhp", "123");
		jedis.hset(key2, "addhp2", "1233");
		jedis.hset(key2, "addhp3", "1243");
		// return resource;
		if(jedis.zrank("lastdirtytime", "1") == null)// no dirty here
		{
			jedis.zadd("lastdirtytime", System.currentTimeMillis(),"1");
		}
		// record dirty column
		// when finsh flush to db,
//		Set<String> mems = jedis.smembers("u:1:item:what");
//		Iterator<String> i = mems.iterator();
//		while(i.hasNext())
//		{
//			jedis.srem("u:1:item:what", i.next());
//		}
		jedis.sadd("u:1:item:dirty_c", "addhp");
		jedis.sadd("u:1:item:dirty_c", "addhp2");
		jedis.sadd("u:1:item:dirty_c", "addhp3");
		
		GameServer.cacheMgr.returnResource(jedis);
		return true;
	}
	
	public boolean handleRegiste(String msg, Channel ch) {
		
		// here need double write,bag get an item can write to cache first,and mark dirty,in crontab ,we will write back to db
		// but user register is an important part of it,so do not just write in cache
		RegisterProtocol rp = (RegisterProtocol) new RegisterProtocol().fromJObj(msg);
		Map param = new HashMap();
		param.put("username", rp.getUn());
		param.put("password", rp.getPass());
		param.put("sex", 1);//rp.getSex());
		param.put("ico", 1);//rp.getIco());
		param.put("nm", "kael");//rp.getNm());
		int result = userService.register(param);
		
		OperateResultProtocol op=null;
		if(result == -1)// failed
		{
			op = new OperateResultProtocol(ErrorCode.ERROR_REGISTE_FAILED,false);
			ch.write(op);
		}
		else
		{
			// SUCCESS,UID
			RegisterLoginSuccessProtocol rsp = new RegisterLoginSuccessProtocol();
			rsp.setUid(result);
			ch.write(rsp);
			
			// save to cache
			Jedis jedis = GameServer.cacheMgr.getResource();
			String key = "u:"+result;
			jedis.hset(key, "un", rp.getUn());
			jedis.hset(key, "pass", rp.getPass());
			jedis.hset(key, "sex", String.valueOf(1));
			jedis.hset(key, "nm", "kael");
			jedis.hset(key, "ico",  String.valueOf(1));
			// return resource;
			GameServer.cacheMgr.returnResource(jedis);
		}
		return (op == null);
	}
	
	public boolean handleDelete(String msg, Channel ch) {
		// need delete directly
		DeleteUserProtocol dp = (DeleteUserProtocol) new DeleteUserProtocol().fromJObj(msg);
		userService.deleteuser(dp.getUid());
		OperateResultProtocol op = new OperateResultProtocol(dp.getCmd(), true);
		ch.write(op);
		// delete cache or may be not delete the hash!
		Jedis jedis = GameServer.cacheMgr.getResource();
//		String key = "u:"+dp.getUid();
		Set<String> mems = jedis.smembers("u:1:info_dirty");
		Iterator<String> i = mems.iterator();
		while(i.hasNext())
		{
			jedis.srem("u:1:info_dirty", i.next());
		}
		
		jedis.zrem("lastdirtytime", String.valueOf(dp.getUid()));
		
		Set<String> val = jedis.hkeys("u:1");
		Iterator<String> i2 = mems.iterator();
		while(i.hasNext())
		{
			jedis.hdel("u:1", i2.next());
		}
		return true;
	}
	
	public boolean handleLogin(String msg, Channel ch) {
		
		LoginProtocol lp = (LoginProtocol) new LoginProtocol().fromJObj(msg);
		Map param = new HashMap();
		param.put("username", lp.getUn());
		param.put("password", lp.getPass());
		int result = userService.login2(param);
		OperateResultProtocol op = null;
		if( result > 0 )// have user uid is result
		{
			
			UserDetailDto ud = getUserDetail(result);
			if(ud == null)
				op = new OperateResultProtocol(ErrorCode.ERROR_LOGIN_FAILED,false);
			else // do with ud,send back to user for example
				;
		}
		else
		{
			op = new OperateResultProtocol(ErrorCode.ERROR_LOGIN_FAILED,false);
		}
		if(op!=null)
			ch.write(op);
		return (op == null);
	}
	
	public boolean handleUpdate(String msg, Channel ch) {
		UpdateUserInfoProtocol uuip = (UpdateUserInfoProtocol) new UpdateUserInfoProtocol().fromJObj(msg);
		int uid = (Integer) uuip.getD().get("uid");
		// is in cache or not
		Jedis jedis = GameServer.cacheMgr.getResource();
		String key = "u:"+uid;
		UserDetailDto ud;
		// is it already in cache? -- other one query user,will save to cache?
		if(jedis.hkeys(key).size() > 0)
		{
			jedis.hset(key, "sex", (String) uuip.getD().get("sex"));
			jedis.hset(key, "nm", (String) uuip.getD().get("nm"));
			jedis.hset(key, "ico", (String) uuip.getD().get("ico"));
			// is first time dirty or not
			if(jedis.zrank("lastdirtytime", "1") == null)// no dirty here
			{
				jedis.zadd("lastdirtytime", System.currentTimeMillis(),"1");
			}
			// record dirty column
			jedis.sadd("u:1:info_dirty", "sex");
			jedis.sadd("u:1:info_dirty", "nm");
			jedis.sadd("u:1:info_dirty", "ico");
		}
		else // wtf,not in cache and update?
		{
			
		}
		return true;
	}
}

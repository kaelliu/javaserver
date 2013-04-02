package lib.kael;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import lib.kael.util.SerializeUtil;

import org.apache.ibatis.cache.Cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisCache implements Cache{

	private Jedis redisClient = createReids();// initial here,not good well 
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();   
	private String id; 
	public RedisCache(final String id) {    
        if (id == null) {  
            throw new IllegalArgumentException("Cache instances require an ID");  
        }  
        this.id = id;  
    }    
	@Override
	public void clear() {
		redisClient.flushDB(); 
	}

	@Override
	public String getId() {
		return this.id; 
	}

	@Override
	public Object getObject(Object key) {
		Object value = SerializeUtil.unserialize(redisClient.get(SerializeUtil.serialize(key.toString())));  
		return value;
	}

	@Override
	public ReadWriteLock getReadWriteLock() {
		return readWriteLock;
	}

	@Override
	public int getSize() {
		return Integer.valueOf(redisClient.dbSize().toString());  
	}

	@Override
	public void putObject(Object key, Object value) {
		 redisClient.set(SerializeUtil.serialize(key.toString()), SerializeUtil.serialize(value));
	}

	@Override
	public Object removeObject(Object key) {
		return redisClient.expire(SerializeUtil.serialize(key.toString()),0);
	}

	protected  static Jedis createReids(){  
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1");  
        return pool.getResource();  // should use like this,this way in mybatis will be a way of website,but game data will change more quickly
    } 
	
	protected  static Jedis getCLient(JedisPool pool){  
        return pool.getResource();  
    }
}

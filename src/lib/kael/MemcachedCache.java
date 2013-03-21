package lib.kael;

import java.util.LinkedList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.ibatis.cache.Cache;

public class MemcachedCache implements Cache
{
	// the read write lock
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private String id; 
	private LinkedList<String> cacheKeys = new LinkedList<String>();
	private MemCached mc = MemCached.getInstance();
	public MemcachedCache(String id)
	{
		this.id = id;
	}
	@Override
	public String getId() {
		return id;
	}

	@Override
	public int getSize() {
		return cacheKeys.size();
	}

	// ���������������� 
	@Override
	public void putObject(Object key, Object value) {
		String cacheKay = String.valueOf(key.hashCode());
		if(!cacheKeys.contains(cacheKay))
		{
			cacheKeys.add(cacheKay);
		}
		mc.add(cacheKay, value);
	}

	// ���� key �ӻ����л�ȡ����  
	@Override
	public Object getObject(Object key) {
		String cacheKey = String.valueOf(key.hashCode());
		Object value = mc.get(cacheKey);
		if(!cacheKeys.contains(cacheKey))
		{
			cacheKeys.add(cacheKey);
		}
		return value;
	}

	public boolean hasKey(Object key) {
		return cacheKeys.contains(key);
	}

	// �ӻ�����ɾ��ָ�� key ����  
	@Override
	public Object removeObject(Object key) {
		String cacheKey = String.valueOf(key.hashCode()); 
		cacheKeys.remove(cacheKey);
		return mc.remove(cacheKey);
	}

	//��յ�ǰ Cache ʵ���е����л�������  
	@Override
	public void clear() {
		for (int i = 0; i < cacheKeys.size(); i++){  
			String cacheKey = cacheKeys.get(i);  
			mc.remove(cacheKey);
		}
		cacheKeys.clear();
	}

	@Override
	public ReadWriteLock getReadWriteLock() {
		return readWriteLock;
	}
}
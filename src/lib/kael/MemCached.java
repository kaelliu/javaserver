package lib.kael;
import java.util.Date;
import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

public class MemCached
{
	// ����ʵ��
	protected static MemCachedClient mcc = new MemCachedClient();
	protected static MemCached mc = new MemCached();
	// �����뻺������������ӳ�
	static
	{
		String[] servers={"127.0.0.1:11211"};
		Integer[] weights ={3};
		// ��ȡ���ӳ�ʵ�����
		SockIOPool pool = SockIOPool.getInstance();
		
		// ���÷�������Ϣ
		pool.setServers(servers);
		pool.setWeights(weights);
		// ���ó�ʼ����������С������������������������ʱ��
		pool.setInitConn(5);
		pool.setMinConn(5);
		pool.setMaxConn(250);
		pool.setMaxIdle(1000 * 60 * 60 * 6);
		// �������߳�˯��ʱ��
		pool.setMaintSleep(30);
		
		// ����TCP����
		pool.setNagle(false);
		pool.setSocketTO(3000);
		pool.setSocketConnectTO(0);
		// ��ʼ�����ӳ�
		pool.initialize();
		
		// ѹ�����ã�����ָ����СK�Ķ��ᱻѹ��
//		mcc.setCompressEnable(true);
//		mcc.setCompressThreshold(64);
	}
	// ����
	protected MemCached()
	{}
	public static MemCached getInstance()
	{
		return mc;
	}
	// ���һ��ָ����ֵ��������
	public boolean add(String key,Object value)
	{
		return mcc.add(key, value);
	}
	public boolean add(String key,Object value,Date expiry)
	{
		return mcc.add(key, value,expiry);
	}
	// �滻
	public boolean replace(String key,Object value)
	{
		return mcc.replace(key, value);
	}
	public boolean replace(String key,Object value,Date expiry)
	{
		return mcc.replace(key, value,expiry);
	}
	// ɾ��	
	public Object remove(String key)
	{
		Object value = mcc.get(key);
		if(mcc.delete(key))
			return value;
		else
			return null;
	}
	// ��ȡ����
	public Object get(String key)
	{
		return mcc.get(key);
	}
}
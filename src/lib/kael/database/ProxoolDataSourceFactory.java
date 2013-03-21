package lib.kael.database;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.DataSourceFactory;
import org.logicalcobwebs.proxool.ProxoolDataSource;

public class ProxoolDataSourceFactory implements DataSourceFactory{
	protected ProxoolDataSource dataSource;
	
	public ProxoolDataSourceFactory() 
	{
		this.dataSource = new ProxoolDataSource();     
	}
	
	@Override
	public DataSource getDataSource() {
		// 
		return this.dataSource;
	}

	@Override
	public void setProperties(Properties properties) {
		// 
		dataSource.setDriver(properties.getProperty("driver"));
		dataSource.setDriver(properties.getProperty("driverUrl"));
		dataSource.setDriver(properties.getProperty("user"));
		dataSource.setDriver(properties.getProperty("password"));
		 //�̱߳�������ʱ�䣬house keeper�������������ӵ�״̬���������Ƿ���Ҫ���ٻ��ߴ�����Ĭ��ʱ��Ϊ30��
        if(properties.containsKey("houseKeepingSleepTime")){
            dataSource.setHouseKeepingSleepTime(Integer.parseInt(properties.get("houseKeepingSleepTime").toString()));
        }
        //���house keep �̷߳��ֿ��е����ӣ��������ʹ�����sql���в��ԣ�������Ӧ�ÿ��ٵı�ִ�С����ѯ���ڵ�sql��䡣
        if(properties.containsKey("houseKeepingTestSql")){
            dataSource.setHouseKeepingTestSql(properties.get("houseKeepingTestSql").toString());
        }
        //����̻߳ʱ�䡣
        //���housekeeper ����һ���̻߳ʱ�䳬�������ʱ�䣬������ֹ����̡߳�
        //��������Ҫ�������ʱ�����Ԥ��������Ӧ��ʱ��(Ĭ��ʱ����5����)��
        if(properties.containsKey("maximumActiveTime")){
            dataSource.setMaximumActiveTime(Integer.parseInt(properties.get("maximumActiveTime").toString()));
        }
        //���ݿ������������Ĭ��ֵΪ15��
        if(properties.containsKey("maximumConnectionCount")){
            dataSource.setMaximumConnectionCount(Integer.parseInt(properties.get("maximumConnectionCount").toString()));
        }
        //һ�����Ӵ��ڵ�����ֻ��ʱ�䡣Ĭ��ֵ��4Сʱ����λ�Ǻ��롣
        if(properties.containsKey("maximumConnectionLifetime")){
            dataSource.setMaximumConnectionLifetime(Integer.parseInt(properties.get("maximumConnectionLifetime").toString()));
        }
        //��С���ӱ��ִ򿪵ĸ����������Ƿ���Ҫ��Ĭ��ֵ��5����
        if(properties.containsKey("minimumConnectionCount")){
            dataSource.setMaximumConnectionLifetime(Integer.parseInt(properties.get("minimumConnectionCount").toString()));
        }
        //�����������ȷ�����ӳص�״̬����������ʱ�䷧ֵ�ڣ���λΪ���룩�ܾ���һ�����ӣ�����Ϊ�ǹ����ˡ�Ĭ��ʱ��60�롣
        if(properties.containsKey("overloadWithoutRefusalLifetime")){
            dataSource.setMaximumConnectionLifetime(Integer.parseInt(properties.get("overloadWithoutRefusalLifetime").toString()));
        }      
	} 
	
}
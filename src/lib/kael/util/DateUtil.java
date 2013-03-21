package lib.kael.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	//ʱ���ʽ
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Calendar cld1 = null;		//ʱ��1
	private static Calendar cld2 = null;		//ʱ��2
	
	/**
	 * �Ƚ�����ֵ�Ǵ�С
	 * @param time1	�磺2012-3-2 15:40:52
	 * @param time2	�磺2012-3-2 15:40:52
	 * @return 0 �����; 1��time1 > time2; -1: time1 < time2
	 */
	public static int compare(String time1, String time2)
	{
		cld1 = Calendar.getInstance();		//ʱ��1
		cld2 = Calendar.getInstance();		//ʱ��2
		try  
		{   
			cld1.setTime(df.parse(time1));   
			cld2.setTime(df.parse(time2));   
		}catch(java.text.ParseException e){   
			System.err.println("��ʽ����ȷ");
			e.printStackTrace();
		}   
		int result=time1.compareTo(time2);   
		if(result==0)   return 0;
		else if(result<0)   return -1;
		else	return 1;  
	}
	
	/**
	 * ��ʱ��ת����date
	 * @param time
	 * @return
	 */
	public static Date getDate(String time){
		cld1 = Calendar.getInstance();		//ʱ��1
		try {
			cld1.setTime(df.parse(time));
		} catch (ParseException e) {
			System.err.println("��ʽ����ȷ");
			e.printStackTrace();
		}
		return cld1.getTime();
	}
	
	/**
	 * ����ϵͳ���
	 * @return 1~12
	 */
	public static int getYear(){
		cld1 = Calendar.getInstance();		//ʱ��1
		return cld1.get(Calendar.YEAR);
	}
	
	/**
	 * ϵͳ�·�
	 * @return 1~12
	 */
	public static int getMonth(){
		cld1 = Calendar.getInstance();		//ʱ��1
		return cld1.get(Calendar.MONTH)+1;
	}
	
	/**
	 * ϵͳ����
	 * @return 1~31
	 */
	public static int getDate(){
		cld1 = Calendar.getInstance();		//ʱ��1
		return cld1.get(Calendar.DATE);
	}
	
	/**
	 * ���ڼ�
	 * @return 1~7
	 */
	public static int getDayOfWeek(){
		cld1 = Calendar.getInstance();		//ʱ��1
		return cld1.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * ��ǰСʱ
	 * @return
	 */
	public static int getHourOfDay(){
		cld1 = Calendar.getInstance();		//ʱ��1
		return cld1.get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * ��ǰ����
	 * @return
	 */
	public static int getMinute(){
		cld1 = Calendar.getInstance();		//ʱ��1
		return cld1.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * ȡ��ϵͳ��ǰʱ��
	 * @return �磺2012-3-2 15:40:52
	 */
	public static String getTime(){
		cld1 = Calendar.getInstance();		//ʱ��1
		return cld1.get(Calendar.YEAR) + "-" + 
			  (cld1.get(Calendar.MONTH)+1) + "-" + 
			   cld1.get(Calendar.DATE) + " " + 
			   cld1.get(Calendar.HOUR_OF_DAY) + ":" + 
			   cld1.get(Calendar.MINUTE) + ":" + 
			   cld1.get(Calendar.SECOND) ;
	}
	
	/**
	 * ��ȡ��ǰϵͳ������
	 * @return �磺2012-3-2
	 */
	public static String getYear_Month_Date(){
		cld1 = Calendar.getInstance();		//ʱ��1
		return cld1.get(Calendar.YEAR) + "-" + 
			  (cld1.get(Calendar.MONTH)+1) + "-" + 
			   cld1.get(Calendar.DATE);
	}
}

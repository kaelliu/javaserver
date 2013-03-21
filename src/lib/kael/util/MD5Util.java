package lib.kael.util;

import java.security.NoSuchAlgorithmException;

public class MD5Util
{
	public static String getMD5(String src)
	{
		String s = null;
		char hexDigits[] = { // �������ֽ�ת���� 16 ���Ʊ�ʾ���ַ�
		            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		 try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(src.getBytes());
            byte tmp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {    // �ӵ�һ���ֽڿ�ʼ���� MD5 ��ÿһ���ֽ�
                // ת���� 16 �����ַ���ת��
                byte byte0 = tmp[i];  // ȡ�� i ���ֽ�
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];  // ȡ�ֽ��и� 4 λ������ת��, 
                // >>> Ϊ�߼����ƣ�������λһ������
                str[k++] = hexDigits[byte0 & 0xf];   // ȡ�ֽ��е� 4 λ������ת��
            }
            s = new String(str);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return s;
	}
}
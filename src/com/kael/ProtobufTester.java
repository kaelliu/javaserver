package com.kael;

import com.data.PersonInfo;
import com.data.UserInfo;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class ProtobufTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// pack
		UserInfo.uInfo.Builder b = UserInfo.uInfo.newBuilder();
        b.setUid(1);
        b.setSex(2);
        b.setIco(3);
        b.setNn("hello");
        b.setUn("jack");
        b.setPass("md5");
        UserInfo.uInfo data = b.build();
        byte [] value = data.toByteArray();
		// unpack
	    try {
			UserInfo.uInfo last = UserInfo.uInfo.parseFrom(value);
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	   
	    PersonInfo.AddressBook.Builder ba = PersonInfo.AddressBook.newBuilder();
	    for(int i=0;i<10;++i)
	    {
	    	PersonInfo.Person.Builder bp = PersonInfo.Person.newBuilder();
	    	bp.setEmail("sss"+i+"@gmail.com");
	    	bp.setName("kael"+i);
	    	bp.setId(i);
	    	PersonInfo.Person.PhoneNumber.Builder pn = PersonInfo.Person.PhoneNumber.newBuilder();
	    	pn.setNumber("123");
	    	bp.addPhone(pn);
//	    	bp.setUnsure();	
		    ba.addPerson(bp);// list
	    }
	    PersonInfo.AddressBook data2 = ba.build();
	    byte [] value2 = data2.toByteArray();
	    
	    int size = value2.length;
	    
	    try {
	    	PersonInfo.AddressBook lastAdd = PersonInfo.AddressBook.parseFrom(value);
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

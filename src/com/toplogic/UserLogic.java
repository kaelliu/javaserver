package com.toplogic;

import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.channel.Channel;

import com.kael.protocol.DeleteUserProtocol;
import com.kael.protocol.ErrorCode;
import com.kael.protocol.LoginProtocol;
import com.kael.protocol.OperateResultProtocol;
import com.kael.protocol.ProtocolMatch;
import com.kael.protocol.RegisterLoginSuccessProtocol;
import com.kael.protocol.RegisterProtocol;
import com.kael.protocol.UpdateUserInfoProtocol;
import com.service.UserService;

public class UserLogic {
	private UserService userService;
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public boolean handleRegiste(String msg, Channel ch) {
		
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
		}
		return (op == null);
	}
	
	public boolean handleDelete(String msg, Channel ch) {
		
		DeleteUserProtocol dp = (DeleteUserProtocol) new DeleteUserProtocol().fromJObj(msg);
		userService.deleteuser(dp.getUid());
		OperateResultProtocol op = new OperateResultProtocol(dp.getCmd(), true);
		ch.write(op);
		return false;
	}
	
	public boolean handleLogin(String msg, Channel ch) {
		
		LoginProtocol lp = (LoginProtocol) new LoginProtocol().fromJObj(msg);
		Map param = new HashMap();
		param.put("username", lp.getUn());
		param.put("password", lp.getPass());
		int result = userService.login(param);
		
		OperateResultProtocol op = null;
		if(result == 1)// extractly match
		{
			int uid = userService.getuid(lp.getUn());
			RegisterLoginSuccessProtocol rsp = new RegisterLoginSuccessProtocol();
			rsp.setUid(uid);
			rsp.setCmd(ProtocolMatch.PS_CMD_LOGIN);
			ch.write(rsp);
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
		userService.changeUser(uuip.getD());
		
		ch.write(uuip);
		return true;
	}
}

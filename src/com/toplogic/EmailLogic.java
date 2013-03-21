package com.toplogic;

import org.jboss.netty.channel.Channel;

//import com.kael.protocol.ChangeEmailProtocol;
import com.service.EmailService;

public class EmailLogic {
	private EmailService emailService;
	public boolean handleChangeEm(String msg, Channel ch) {
//		ChangeEmailProtocol cep = (ChangeEmailProtocol) new ChangeEmailProtocol().fromJObj(msg);
//		int eid = cep.getEid();
//		Map param = new HashMap();
//		param.put("id", eid);
//		param.put("st",1);
//		emailService.changeEmail(param);
		return false;
	}
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
	public EmailService getEmailService() {
		return emailService;
	}
}

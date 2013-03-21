package com.service;

import java.util.List;
import java.util.Map;

public class EmailServiceImpl implements EmailService
{
	private EmailDao emailDao;

	public EmailDao getEmailDao() {
		return emailDao;
	}

	public void setEmailDao(EmailDao emailDao) {
		this.emailDao = emailDao;
	}

	@Override
	public boolean createEmail(Map param) {
		// 
		emailDao.createEmail(param);
		return true;
	}

	@Override
	public boolean changeEmail(Map param) {
		// 
		emailDao.changeEmail(param);
		return true;
	}

	@Override
	public boolean deleteEmail(int id) {
		//
		emailDao.deleteEmail(id);
		return true;
	}

	@Override
	public EmailDto getEmail(int id) {
		return emailDao.getEmail(id);
	}

	@Override
	public List<EmailDto> getMyEmail(int rid) {
		return emailDao.getMyEmail(rid);
	}
	
}
package com.service;

import java.util.List;
import java.util.Map;

public interface EmailDao
{
	public EmailDto getEmail(int id);
	public List<EmailDto> getMyEmail(int rid);
	public void createEmail(Map param);
	public void changeEmail(Map param);
	public void deleteEmail(int id);
}

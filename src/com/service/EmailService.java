package com.service;

import java.util.List;
import java.util.Map;

public interface EmailService
{
	public EmailDto getEmail(int id);
	public List<EmailDto> getMyEmail(int rid);
	public boolean createEmail(Map param);
	public boolean changeEmail(Map param);
	public boolean deleteEmail(int id);
}
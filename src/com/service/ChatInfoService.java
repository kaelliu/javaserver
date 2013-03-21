package com.service;

import java.util.List;
import java.util.Map;

public interface ChatInfoService {
	public ChatInfoDto getChatInfo(int id);
	public List<ChatInfoDto> getmyChat(Map param);
	public void createChatInfo(Map param);
	public void changeChatInfo(Map param);
	public void deleteChatInfo(int id);
}

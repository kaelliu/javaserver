package com.service;

import java.util.List;
import java.util.Map;

public class ChatInfoServiceImpl implements ChatInfoService
{
	private ChatInfoDao chatInfoDao;
	@Override
	public ChatInfoDto getChatInfo(int id) {
		return chatInfoDao.getChatInfo(id);
	}

	@Override
	public List<ChatInfoDto> getmyChat(Map param) {
		
		return chatInfoDao.getmyChat(param);
	}

	@Override
	public void createChatInfo(Map param) {
		chatInfoDao.createChatInfo(param);
		
	}

	@Override
	public void changeChatInfo(Map param) {
		chatInfoDao.changeChatInfo(param);
		
	}

	@Override
	public void deleteChatInfo(int id) {
		chatInfoDao.deleteChatInfo(id);
		
	}

	public void setChatInfoDao(ChatInfoDao chatInfoDao) {
		this.chatInfoDao = chatInfoDao;
	}

	public ChatInfoDao getChatInfoDao() {
		return chatInfoDao;
	}

}

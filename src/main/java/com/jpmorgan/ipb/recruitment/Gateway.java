package com.jpmorgan.ipb.recruitment;

import com.jpmorgan.ipb.recruitment.listener.MessageStatusListener;

public interface Gateway {
	
	public void send(Message message);
	public void setMessageStatusListener(MessageStatusListener messageStatusListener);
}
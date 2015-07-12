package com.jpmorgan.ipb.recruitment.resource;

import com.jpmorgan.ipb.recruitment.Message;

/**
 * This class protects resources providing access to them only when resources are freeto serve.
 * 
 * @author Touqir
 *
 */
public interface MessageResourceManager {

	/**
	 * sends message to Gateway when resource is free.
	 * 
	 * @param message
	 */
	public void sendMessage(Message message);
}

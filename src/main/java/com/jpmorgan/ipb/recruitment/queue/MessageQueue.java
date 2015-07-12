package com.jpmorgan.ipb.recruitment.queue;

import com.jpmorgan.ipb.recruitment.Message;

/** This interface defines methods to implement message prioritization and 
 * retrieval algorithm.
 *  
 * @author Touqir
 *
 */
public interface MessageQueue {

	/** 
	 * saves message to be processed later
	 * @param message
	 */
	public void offer(Message message);
	 
	/**
	 * retrieves and removes message from queue
	 * @return
	 */
	public Message poll();
	
	/**
	 * removes group of messages from queue on user request.
	 * 
	 * @param groupId
	 */
	public void cancelMessageGroup(int groupId);
	
}

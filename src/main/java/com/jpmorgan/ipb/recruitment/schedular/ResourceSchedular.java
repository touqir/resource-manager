package com.jpmorgan.ipb.recruitment.schedular;

import com.jpmorgan.ipb.recruitment.Message;

/**
 * Resource schedular acts as a Facade for client. It receives messages from client 
 * and pass on to the message queue where those messages are prioritized using 
 * a particular algorithm. It also encapsulate thread that works continously and 
 * responsible to transfer messages from message queue to gateway.
 * 
 * @author Touqir
 *
 */
public interface ResourceSchedular {

	/** 
	 * adds message to message queue
	 * 
	 * @param message
	 */
	public void addMessage(Message message);
	
	/**
	 * sets a given message group as canceled so that 
	 * no more messages will be processed from that group.
	 * 
	 * @param groupId message group id
	 */
	public void cancelMessageGroup(int groupId);
	
	/**
	 * starts the message processing thread that  
	 * polls and processes messages from message queue.
	 */
	public void start();
	
	/**
	 * cleanup the message processing thread and other resources.
	 */
	public void shutdown();
}

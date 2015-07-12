package com.jpmorgan.ipb.recruitment.queue;

import com.jpmorgan.ipb.recruitment.Message;

/**
 * Validates the messages before their processing.
 * 
 * @author Touqir
 *
 */
public interface MessageGroupValidator {

	/**
	 * This method validates the input message whether it can be 
	 * processed further or not.
	 * 
	 * @param message
	 * 
	 * @throws GroupTerminatedException
	 * 			when message is received of a group for which 
	 * 			termination message was already received.
	 * 
	 * @throws GroupCanceledException
	 * 			when message is received of a group that has canceled
	 */
	public void validateMessageGroup(Message message);
	
	/**
	 * sets a particular group as terminated , means no more messages will 
	 * be accepted for that group.
	 * 
	 * @param groupId
	 */
	public void setMessageGroupTerminated(int groupId);
	
	/**
	 * sets a particular group as canceled , means no more messages will 
	 * be accepted for that group.
	 * 
	 * @param groupId
	 */
	public void setMessageGroupCanceled(int groupId);
	
	/**
	 * @param groupId
	 * 
	 * @return true if group has been terminated else returns false
	 */
	public boolean isMessageGroupTerminated(int groupId);
	
	/**
	 * @param groupId
	 * 
	 * @return true if group has been canceled else returns false
	 */
	public boolean isMessageGroupCanceled(int groupId);
	
	/**
	 * @param message
	 * 
	 * @return true if message is of type Termination Message else return false.
	 */
	public boolean isTerminationMessage(Message message);
}

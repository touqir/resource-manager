package com.jpmorgan.ipb.recruitment.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.jpmorgan.ipb.recruitment.Message;
import com.jpmorgan.ipb.recruitment.util.ParameterValidator;

/**
 * Message Queue implementation that process the messages in the same order they are received.
 * This class doesn't prioritize any messages.
 * 
 * @author Touqir
 *
 */
public class SimpleMessageQueue implements MessageQueue {

	final static Logger logger = Logger.getLogger(SimpleMessageQueue.class);
	
	/** checks whether message can be processed or not. */
	private MessageGroupValidator messageGroupValidator;
	
	/** Blocking queue in which messages are put and retrieved  */
	private BlockingQueue<Message> messageQueue;

	public SimpleMessageQueue(MessageGroupValidator messageGroupValidator) {
		this.messageGroupValidator = messageGroupValidator;
		ParameterValidator.throwExceptionOnNullValue(messageGroupValidator,
				"MessageGroupValidator dependency should not be null.", logger);
		
		messageQueue = new LinkedBlockingQueue<Message>();
	}
	
	@Override
	public void offer(Message message) {
		messageGroupValidator.validateMessageGroup(message); 
		try {
			messageQueue.put(message);
		} catch (InterruptedException e) {}
	}

	@Override
	public Message poll() { 
		Message message = null;
		
		while(message == null) {
			try {
				message = messageQueue.take();
			} catch (InterruptedException e) {}
		}

		if( messageGroupValidator.isMessageGroupCanceled(message.getGroupId()) ) {
			return null;
		}
		
		return message;
	}

	@Override
	public void cancelMessageGroup(int groupId) {
		messageGroupValidator.setMessageGroupCanceled(groupId); 
	}

}

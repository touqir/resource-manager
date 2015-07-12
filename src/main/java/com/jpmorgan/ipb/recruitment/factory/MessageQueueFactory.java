package com.jpmorgan.ipb.recruitment.factory;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jpmorgan.ipb.recruitment.factory.types.MessageQueueType;
import com.jpmorgan.ipb.recruitment.queue.GroupPriorityMessageQueue;
import com.jpmorgan.ipb.recruitment.queue.MessageGroupValidator;
import com.jpmorgan.ipb.recruitment.queue.MessageGroupValidatorImpl;
import com.jpmorgan.ipb.recruitment.queue.MessageQueue;
import com.jpmorgan.ipb.recruitment.queue.SimpleMessageQueue;
import com.jpmorgan.ipb.recruitment.util.ParameterValidator;

/**
 * Factory class implementing factory-method design pattern that will create instances of 
 * specific message queue implementation
 * 
 * @author touqir
 *
 */
public class MessageQueueFactory {

	final static Logger logger = Logger.getLogger(MessageQueueFactory.class);

	public static MessageQueue getMessageQueue(MessageQueueType queueType) {
		ParameterValidator.throwExceptionOnNullValue(queueType,
				"MessageQueueType enum value should not be null.", logger);
		
		MessageQueue messageQueue;
		switch(queueType) {
			case GROUP_PRIORITY_MESSAGE_QUEUE:
				messageQueue = createGroupPriorityMessageQueue();
				break;
			case SIMPLE_MESSAGE_QUEUE:
				messageQueue = createSimpleMessageQueue();
				break;
			default:
				messageQueue = createGroupPriorityMessageQueue();
				break;
		}
		
		return messageQueue;
	}
	
	private static MessageQueue createGroupPriorityMessageQueue() {
		List<Integer> terminatedGroupList = new ArrayList<Integer>();
		List<Integer> canceledGroupList = new ArrayList<Integer>();
		MessageGroupValidator messageGroupValidator = new MessageGroupValidatorImpl(terminatedGroupList, canceledGroupList);
		MessageQueue messageQueue = new GroupPriorityMessageQueue(messageGroupValidator);
		logger.info("Creatig a GroupPriorityMessageQueue"); 
		return messageQueue;
	}

	private static MessageQueue createSimpleMessageQueue() {
		List<Integer> terminatedGroupList = new ArrayList<Integer>();
		List<Integer> canceledGroupList = new ArrayList<Integer>();
		MessageGroupValidator messageGroupValidator = new MessageGroupValidatorImpl(terminatedGroupList, canceledGroupList);
		MessageQueue messageQueue = new SimpleMessageQueue(messageGroupValidator);
		logger.info("Creatig a SimpleMessageQueue"); 
		return messageQueue;
	}

}

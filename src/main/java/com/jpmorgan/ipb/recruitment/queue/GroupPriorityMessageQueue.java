package com.jpmorgan.ipb.recruitment.queue;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.log4j.Logger;

import com.jpmorgan.ipb.recruitment.Message;
import com.jpmorgan.ipb.recruitment.util.ParameterValidator;

/**
 * This class implements the message prioritization algorithm to select the next message from
 * queue. Messages are prioritized on the basis of their group ids and message group insertion order.
 * This is a blocking synchronous queue that can either be accessed by producer or consumer at one time.
 * 
 *  This queue internally stores the messages in LinkedHashMap that maintains elements insertion order.
 *  key is a groupId and value is a linkedList storing all the messages of a partcular group.
 * 
 * @author Touqir
 *
 */
public class GroupPriorityMessageQueue implements MessageQueue {

	final static Logger logger = Logger.getLogger(GroupPriorityMessageQueue.class);

	/** map that stores message group and all its messages as a single entry  */
	private Map<Integer, Queue<Message>> messageGroupMap;
	
	/** checks whether message can be processed or not. */
	private MessageGroupValidator messageGroupValidator; 
	
	private volatile boolean waiting;
	
	/** integer representing the current group to retrieve its messages and send them to resources for processing  */
	private Integer currentGroup;
	
	public GroupPriorityMessageQueue(MessageGroupValidator messageGroupValidator) {
		this.messageGroupValidator = messageGroupValidator;
		ParameterValidator.throwExceptionOnNullValue(messageGroupValidator,
				"MessageGroupValidator dependency should not be null.", logger);
		
		// linked hashmap maintain insertion order
		messageGroupMap = new LinkedHashMap<Integer, Queue<Message>>();
	}
	
	@Override
	public synchronized void offer(Message message) {
		messageGroupValidator.validateMessageGroup(message); 

		// messages with same groupId will be added in the linkedlist
		Integer groupId = message.getGroupId();
		Queue<Message> groupMessages = messageGroupMap.get(groupId);
		if(groupMessages == null) {
			groupMessages = new LinkedList<Message>();
			messageGroupMap.put(groupId, groupMessages);
		}
		groupMessages.offer(message);
		
		// notify the thread that is waiting for messages to be added
		if(waiting) {
			waiting = false;
			notify();
		}
	}

	@Override
	public synchronized Message poll() { 
		// wait if there are no messages available
		while(messageGroupMap.isEmpty()) {
			waiting = true;
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		
		// work on one group at a time
		if(currentGroup == null)
			currentGroup = getNextGroup();
		
		// retrieve all messages from a single group at a time in order to prevent interleaving the message groups.
		// once group messages are finished then next group messages will be retrieved
		Queue<Message> groupMessages = messageGroupMap.get(currentGroup);
		while(groupMessages.isEmpty()) {
			messageGroupMap.remove(currentGroup);
			currentGroup = getNextGroup();
			groupMessages = messageGroupMap.get(currentGroup);				
		}
		
		// retrieve and remove oldest message from message group
		return groupMessages.poll();
	}
	
	@Override
	public synchronized void cancelMessageGroup(int groupId) {
		messageGroupValidator.setMessageGroupCanceled(groupId); 
		messageGroupMap.remove(groupId);
	}
	
	private int getNextGroup() {
		return messageGroupMap.keySet().iterator().next();
	}
	
}

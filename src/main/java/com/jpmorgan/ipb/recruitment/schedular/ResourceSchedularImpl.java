package com.jpmorgan.ipb.recruitment.schedular;

import org.apache.log4j.Logger;

import com.jpmorgan.ipb.recruitment.Message;
import com.jpmorgan.ipb.recruitment.queue.MessageQueue;
import com.jpmorgan.ipb.recruitment.resource.MessageResourceManager;
import com.jpmorgan.ipb.recruitment.util.ParameterValidator;

/**
 * @author Touqir
 *
 */
public class ResourceSchedularImpl implements ResourceSchedular {

	final static Logger logger = Logger.getLogger(ResourceSchedularImpl.class);

	/** A Custom Queue that stores messages to be processed later */
	private MessageQueue messageQueue;
	
	/** controls access to the resources available */
	private MessageResourceManager messageResourceManager;
	
	/** poll messages from message queue and send them to gateway */
	private MessageProcessingThread messageProcessingThread;
	
	private boolean threadStarted = false;

	public ResourceSchedularImpl(MessageQueue messageQueue,
			MessageResourceManager messageResourceManager) {

		this.messageQueue = messageQueue;
		ParameterValidator.throwExceptionOnNullValue(messageQueue,
				"MessageQueue dependency should not be null.", logger);

		this.messageResourceManager = messageResourceManager;
		ParameterValidator.throwExceptionOnNullValue(messageResourceManager,
				"MessageResourceManager dependency should not be null.", logger);

		messageProcessingThread = new MessageProcessingThread();
	}

	@Override
	public void addMessage(Message message) {
		if (message == null)
			return;
		messageQueue.offer(message);
	}

	@Override
	public void cancelMessageGroup(int groupId) {
		messageQueue.cancelMessageGroup(groupId);
	}

	@Override
	public void start() {
		if(! threadStarted) {
			threadStarted = true;
			messageProcessingThread.start();
		}
	}

	@Override
	public void shutdown() {
		if (messageProcessingThread == null)
			return;
		messageProcessingThread.stopExecution();
	}
	
	MessageProcessingThread getMessageProcessingThread() {
		return messageProcessingThread;
	}

	/**
	 * Thread responsible to transfer message from message queue to Gateway for processing.
	 * This thread will wait in two conditions, first when message queue is empty and second 
	 * when message processing resource is not available. 
	 * 
	 * @author Touqir
	 *
	 */
	class MessageProcessingThread extends Thread {

		private volatile boolean stop;

		public MessageProcessingThread() {
			setName("MessageProcessingThread");
			setDaemon(true); 
		}

		@Override
		public void run() {
			while (!stop) {
				pollAndSendMessageToGateway();
			}
		}
		
		private void pollAndSendMessageToGateway() {
			Message message = messageQueue.poll();
			if(message == null) return;
			messageResourceManager.sendMessage(message);
		}

		public void stopExecution() {
			stop = true;
		}
	}

}

package com.jpmorgan.ipb.recruitment.resource;

import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;

import com.jpmorgan.ipb.recruitment.Gateway;
import com.jpmorgan.ipb.recruitment.Message;
import com.jpmorgan.ipb.recruitment.listener.MessageStatusListener;
import com.jpmorgan.ipb.recruitment.util.ParameterValidator;

/**
 * @author Touqir
 *
 */
public class MessageResourceManagerImpl implements MessageResourceManager, MessageStatusListener {

	final static Logger logger = Logger.getLogger(MessageResourceManagerImpl.class);

	/** protects the resource and provides permit to access it */
	private Semaphore resourceLocks;
	
	/** Gateway sends messages to resources asnchronously */
	private Gateway gateway;

	public MessageResourceManagerImpl(Semaphore resourceLocks, Gateway gateway) {
		this.resourceLocks = resourceLocks;
		ParameterValidator.throwExceptionOnNullValue(resourceLocks, "Semaphore dependency should not be null.", logger);
		
		this.gateway = gateway;
		ParameterValidator.throwExceptionOnNullValue(gateway, "Gateway dependency should not be null.", logger);
		this.gateway.setMessageStatusListener(this);
	}
	
	@Override
	public void sendMessage(Message message) {
		try {
			resourceLocks.acquire();  // get permit to access resource
			gateway.send(message);  // send message to gateway 
		} catch (InterruptedException e) {
			String threadName = Thread.currentThread().getName();
			logger.error(threadName + " interrupted while waiting for lock to resource", e); 
		}
	}
	
	@Override
	public void onMessageComplete() {
		resourceLocks.release();
	}

}

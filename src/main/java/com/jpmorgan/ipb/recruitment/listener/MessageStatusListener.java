package com.jpmorgan.ipb.recruitment.listener;


/**
 * This listener class is registered as a callback with Gateway and 
 * gets invoked whenever message completio takes place.
 * 
 * @author Touqir
 *
 */
public interface MessageStatusListener {

	/**
	 * This method ets called when resource finish processing message.
	 */
	public void onMessageComplete();
}

package com.jpmorgan.ipb.recruitment.queue;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.jpmorgan.ipb.recruitment.Message;

@RunWith(MockitoJUnitRunner.class)
public class SimpleMessageQueueTest {

	@Mock
	private MessageGroupValidator messageGroupValidator;
	@Mock
	private Message message;

	SimpleMessageQueue simpleMessageQueue = null;

	
	@Before
	public void setup() {
		simpleMessageQueue = new SimpleMessageQueue(messageGroupValidator); 
	}
	
	@After
	public void teardown() {
		simpleMessageQueue = null;
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void throwExceptionWhenParameterIsNull() throws Exception {
		new SimpleMessageQueue(null); 
	}

	@Test
	public void canPollMessageFromTheInternalQueue() throws Exception {
		simpleMessageQueue.offer(message);
		
		Message message1 = simpleMessageQueue.poll();
		
		assertSame(message, message1);
		
		verify(messageGroupValidator).validateMessageGroup(message); 
		verify(messageGroupValidator).isMessageGroupCanceled(anyInt());
	}
	
	@Test
	public void canCancelMessageGroupNotToBeProcessedFurther() throws Exception {
		simpleMessageQueue.cancelMessageGroup(1);
		
		verify(messageGroupValidator).setMessageGroupCanceled(anyInt());
		verifyNoMoreInteractions(messageGroupValidator); 
	}
	
}

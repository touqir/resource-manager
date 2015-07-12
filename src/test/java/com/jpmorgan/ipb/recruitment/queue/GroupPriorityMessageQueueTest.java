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
public class GroupPriorityMessageQueueTest {

	@Mock
	private MessageGroupValidator messageGroupValidator;
	
	@Mock
	private Message message;

	GroupPriorityMessageQueue groupPriorityMessageQueue = null;

	
	@Before
	public void setup() {
		groupPriorityMessageQueue = new GroupPriorityMessageQueue(messageGroupValidator); 
	}
	
	@After
	public void teardown() {
		groupPriorityMessageQueue = null;
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void throwExceptionWhenParameterIsNull() throws Exception {
		new GroupPriorityMessageQueue(null); 
	}

	@Test
	public void canPollMessageFromTheInternalQueue() throws Exception {
		groupPriorityMessageQueue.offer(message);
		
		Message message1 = groupPriorityMessageQueue.poll();
		
		assertSame(message, message1);
		
		verify(messageGroupValidator).validateMessageGroup(message);
	}
	
	@Test
	public void canCancelMessageGroupNotToBeProcessedFurther() throws Exception {
		groupPriorityMessageQueue.cancelMessageGroup(1);
		
		verify(messageGroupValidator).setMessageGroupCanceled(anyInt());
		verifyNoMoreInteractions(messageGroupValidator); 
	}
	
}

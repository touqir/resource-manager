package com.jpmorgan.ipb.recruitment.schedular;

import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.jpmorgan.ipb.recruitment.Message;
import com.jpmorgan.ipb.recruitment.factory.MessageQueueFactory;
import com.jpmorgan.ipb.recruitment.factory.types.MessageQueueType;
import com.jpmorgan.ipb.recruitment.queue.MessageQueue;
import com.jpmorgan.ipb.recruitment.resource.MessageResourceManager;

@RunWith(MockitoJUnitRunner.class)
public class ResourceSchedularImplTest {

	@Mock 
	private MessageQueue messageQueue;
	
	@Mock 
	private MessageResourceManager messageResourceManager;
	
	@Mock
	private Message message;

	ResourceSchedular resourceScedular = null;
	
	@Before
	public void setup() {
		resourceScedular = new ResourceSchedularImpl(messageQueue, messageResourceManager); 
	}
	
	@After
	public void teardown() {
		if(resourceScedular != null) 
			resourceScedular.shutdown();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void throwExceptionWhenMessageQueueParameterIsNull() throws Exception {
		resourceScedular = new ResourceSchedularImpl(null, messageResourceManager);
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwExceptionWhenMessageProcessingExecutorParameterIsNull() throws Exception {
		resourceScedular = new ResourceSchedularImpl(messageQueue, null);
	}

	@Test
	public void addMessage_doNothingWhenMessageValueIsNull() throws Exception {
		//when
		resourceScedular.addMessage(null);
		//then
		verify(messageQueue, never()).offer(any(Message.class));
	}
	
	@Test
	public void addMessage_messageAddedWhenMessageIsNotNull() throws Exception {
		//when
		resourceScedular.addMessage(message);
		//then
		verify(messageQueue, times(1)).offer(any(Message.class));
	}
	
	@Test
	public void cancelMessageGroup_shouldCancelMessageGroupWhenCalledWithSpecificGroupId() throws Exception {
		//when
		resourceScedular.cancelMessageGroup(1);
		//then
		verify(messageQueue, times(1)).cancelMessageGroup(anyInt());
	}
	
	@Test
	public void messageProcessingThreadShouldWaitWhenNoMessageToProcess() throws Exception {
		MessageQueue simpleMessageQueue = MessageQueueFactory.getMessageQueue(MessageQueueType.SIMPLE_MESSAGE_QUEUE);
		MessageQueue simpleMessageQueueSpy = spy(simpleMessageQueue);
		ResourceSchedularImpl resourceScedular = new ResourceSchedularImpl(simpleMessageQueueSpy, messageResourceManager);
		ResourceSchedularImpl.MessageProcessingThread messageProcessingThread = resourceScedular.getMessageProcessingThread();
		
		//when
		resourceScedular.start();
		messageProcessingThread.join(1000); // wait for message processing thread to finish for 1 second.
		
		//then
		verify(simpleMessageQueueSpy, times(1)).poll();
		verifyZeroInteractions(messageResourceManager);
	}
	
	@Test
	public void messageProcessingThreadShouldProcessMessageWhenExistsInQueue() throws Exception {
		when(messageQueue.poll()).thenReturn(message).thenThrow(new RuntimeException()); 
		ResourceSchedularImpl resourceScedular = new ResourceSchedularImpl(messageQueue, messageResourceManager);
		ResourceSchedularImpl.MessageProcessingThread messageProcessingThread = resourceScedular.getMessageProcessingThread();
		
		//when
		resourceScedular.start();
		messageProcessingThread.join(100); // wait for message processing thread to finish for 100 millisecond.
		
		//then
		verify(messageQueue, atLeast(2)).poll();
		verify(messageResourceManager, atLeast(1)).sendMessage(any(Message.class));
	}
	
}

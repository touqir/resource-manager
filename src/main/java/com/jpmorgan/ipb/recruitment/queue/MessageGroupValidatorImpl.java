package com.jpmorgan.ipb.recruitment.queue;

import java.util.List;

import org.apache.log4j.Logger;

import com.jpmorgan.ipb.recruitment.Message;
import com.jpmorgan.ipb.recruitment.TerminationMessage;
import com.jpmorgan.ipb.recruitment.exceptions.GroupCanceledException;
import com.jpmorgan.ipb.recruitment.exceptions.GroupTerminatedException;

/**
 * @see MessageGroupValidator
 * 
 * @author Touqir
 *
 */
public class MessageGroupValidatorImpl implements MessageGroupValidator {

	final static Logger logger = Logger.getLogger(MessageGroupValidatorImpl.class);

	/** stores list of terminated groups */
	private List<Integer> terminatedGroupList;
	
	/** stores list of canceled groups */
	private List<Integer> canceledGroupList;
	
	public MessageGroupValidatorImpl(List<Integer> terminatedGroupList, List<Integer> canceledGroupList) {
		this.terminatedGroupList = terminatedGroupList;
		this.canceledGroupList = canceledGroupList;
	}
	
	@Override
	public void validateMessageGroup(Message message) {
		Integer groupId = message.getGroupId();
		
		if (isMessageGroupTerminated(groupId))
			throw new GroupTerminatedException(groupId);

		if (isMessageGroupCanceled(groupId))
			throw new GroupCanceledException(groupId);

		if (isTerminationMessage(message))
			terminatedGroupList.add(groupId);
	}
	
	public void setMessageGroupTerminated(int groupId) {
		if (! isMessageGroupTerminated(groupId))
			terminatedGroupList.add(groupId);
	}
	
	public void setMessageGroupCanceled(int groupId) {
		if (! isMessageGroupCanceled(groupId))
			canceledGroupList.add(groupId);
	}
	
	@Override
	public boolean isMessageGroupTerminated(int groupId) {
		return terminatedGroupList.contains(groupId);
	}

	@Override
	public boolean isMessageGroupCanceled(int groupId) {
		return canceledGroupList.contains(groupId);
	}
	
	@Override
	public boolean isTerminationMessage(Message message) {
		return (message instanceof TerminationMessage);
	}

}

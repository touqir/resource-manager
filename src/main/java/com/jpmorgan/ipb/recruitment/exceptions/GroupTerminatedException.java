package com.jpmorgan.ipb.recruitment.exceptions;

/**
 * This exception is thrown when termination message of a particular group is received.
 * This exception states that no more messages of terminated group will be received from 
 * client.
 * 
 * @author Touqir
 *
 */
public class GroupTerminatedException extends RuntimeException {

	private static final long serialVersionUID = 6562442821982089932L;

	public GroupTerminatedException(Integer groupId) {
		super("No more messages from this group (" + groupId + ") as this group has been terminated");
	}
}

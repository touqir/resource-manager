package com.jpmorgan.ipb.recruitment.exceptions;

/**
 * This exception is thrown on a message whose group has been canceled.
 * 
 * @author Touqir
 *
 */
public class GroupCanceledException extends RuntimeException {

	private static final long serialVersionUID = 8192770243145043786L;

	public GroupCanceledException(Integer groupId) {
		super("No more messages from this group (" + groupId + ") as this group has been canceled");
	}
}

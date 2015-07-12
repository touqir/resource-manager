package com.jpmorgan.ipb.recruitment.util;

import org.apache.log4j.Logger;

public class ParameterValidator {

	private ParameterValidator() {
	}

	public static void throwExceptionOnNullValue(Object reference, String message, Logger logger) {
		if (reference != null)	return;

		IllegalArgumentException exception = new IllegalArgumentException(message);
		logger.error(message, exception);
		throw exception;
	}
}

package com.carfax.blueprint.amqp;

import org.slf4j.Logger;
import org.springframework.util.ErrorHandler;

public class LoggingErrorHandler implements ErrorHandler {

	private final Logger logger;

	public LoggingErrorHandler(Logger logger) {
		this.logger = logger;
	}

	public void handleError(Throwable t) {
		logger.error("Error consuming message", t);

	}

}

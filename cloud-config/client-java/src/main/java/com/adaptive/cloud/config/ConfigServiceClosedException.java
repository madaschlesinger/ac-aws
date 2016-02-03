package com.adaptive.cloud.config;

/**
 * Exception thrown to indicate that an attempt was made to access a config service either before it was opened
 * or after it has been closed.
 * @author Spencer Ward
 */
@SuppressWarnings("serial")
public class ConfigServiceClosedException extends RuntimeException {
	public ConfigServiceClosedException() {
		super("Cannot access a closed config service.");
	}
}

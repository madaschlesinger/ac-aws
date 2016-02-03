package com.adaptive.cloud.config;

/**
 * Exception thrown to indicate that a property's value could not be determined as it uses a placeholder
 * that contains an infinite recursion to other placeholder properties.
 * <p>
 * For example a property <code>foo=${foo}</code> would not be able to provide its value. Accordingly a call to
 * {@code foo.asXYZ()} would be expected to raise a {@code CircularPlaceholderException}.
 * </p>
 * @author Spencer Ward
 */
@SuppressWarnings("serial")
public class CircularPlaceholderException extends RuntimeException {

	public CircularPlaceholderException(String placeholder) {
		super("Cannot resolve circular placeholder '" + placeholder + "'");
	}

}

package com.adaptive.cloud.config;

/**
 * Exception thrown to indicate that a property's value could not be determined as it uses a placeholder that could not be resolved at runtime.
 * <p>
 * For example a property <code>foo=${bar}</code> would not be able to provide its value unless a {@code bar} property were available in the same context. Accordingly a call to
 * {@code foo.asXYZ()} would be expected to raise a {@code UnresolvedPlaceholderException}.
 * </p>
 * @author Kevin Seal
 */
@SuppressWarnings("serial")
public class UnresolvedPlaceholderException extends RuntimeException {

	public UnresolvedPlaceholderException(String placeholder) {
		super("Cannot replace unknown placeholder '" + placeholder + "'");
	}

}

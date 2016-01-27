package com.adaptive.cloud.config;

/**
 * Exception thrown to indicate that a property's value could not be coerced into the desired data type.
 * <p>
 * For example, given the property {@code  foo=bar} we would expect {@code int i = property.asInteger()} to fail by raising an {@code InvalidConversionException}.
 * </p>
 * <p>
 * The specific circumstances under which a value is unable to be converted to the requested data type may depend on the configuration service implementation being used.
 * </p>
 * @author Kevin Seal
 */
@SuppressWarnings("serial")
public class InvalidConversionException extends RuntimeException {

	public InvalidConversionException(String value, Class desiredType) {
		super(String.format("Could not covert property value '%s' into type '%s'", value, desiredType));
	}

}

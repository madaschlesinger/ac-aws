package com.adaptive.cloud.config;

import java.util.List;

/**
 * Interface representing a named property contained within a {@link ConfigNode}.
 * <p>
 * This can be thought of as a key/value pair whose value is one of a limited set of supported data types. It's similar
 * in this respect to JSON nodes from Jackson, GSON and JSR-353 but without support for nested objects.
 * </p>
 * <p>
 * If a property's value can't be coerced into the requested type then a {@link InvalidConversionException} is expected to be thrown.
 * </p>
 * @author Kevin Seal
 */
public interface Property {
	String name();
	String asString();
	Boolean asBoolean();
	Integer asInteger();
	Double asDouble();
	List<String> asStrings();
}

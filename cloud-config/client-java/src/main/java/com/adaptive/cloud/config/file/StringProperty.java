package com.adaptive.cloud.config.file;

import com.adaptive.cloud.config.ConfigNode;
import com.adaptive.cloud.config.InvalidConversionException;
import com.adaptive.cloud.config.Property;

import java.util.List;

/**
 * Represents a property whose value is given as a string, but may represent any data type.
 * <p>
 * The property value may also contain placeholders.  See {@link PlaceholderResolver}.
 * </p>
 * @author Spencer Ward
 */
public class StringProperty implements Property {
    private ConfigNode container;
    private final PlaceholderResolver resolver;
    private final String name;
    private final String value;

    public StringProperty(String name, String value, ConfigNode container, PlaceholderResolver resolver) {
        this.name = name;
        this.value = value;
        this.container = container;
        this.resolver = resolver;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String rawValue() {
        return value;
    }

    @Override
    public String asString() {
        return resolver.resolve(value, container);
    }

    @Override
    public Boolean asBoolean() {
        return Boolean.parseBoolean(asString());
    }

    @Override
    public Integer asInteger() {
        try {
            return Integer.parseInt(asString());
        } catch (NumberFormatException e) {
            throw new InvalidConversionException(asString(), Integer.class);
        }
    }

    @Override
    public Double asDouble() {
        try {
            return Double.parseDouble(asString());
        } catch (NumberFormatException e) {
            throw new InvalidConversionException(asString(), Integer.class);
        }
    }

    @Override
    public List<String> asStrings() {
        throw new UnsupportedOperationException();
    }
}


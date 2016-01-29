package com.adaptive.cloud.config.file;

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
class StringProperty implements Property {
    private PlaceholderResolver resolver;
    private String name;
    private String value;

    StringProperty(String name, String value, PlaceholderResolver resolver) {
        this.name = name;
        this.value = value;
        this.resolver = resolver;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String asString() {
        return resolver.resolve(value);
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

package com.adaptive.cloud.config.file;

import com.adaptive.cloud.config.ConfigNode;
import com.adaptive.cloud.config.ConfigService;
import com.adaptive.cloud.config.Property;
import com.adaptive.cloud.config.UnresolvedPlaceholderException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A means of resolving placeholders within a property value by looking within other configuration sources
 * <p>
 * This class will evolve to handle intelligent searching for placeholders, but at this stage it looks
 * only on the sources which have been registered.
 * </p>
 * @author Spencer Ward
 * TODO: Handle infinite recursion in property resolution
 */
public class PlaceholderResolver {
    private Collection<ConfigService> sources = new ArrayList<>();

    /**
     * Adds a source of properties which can be used to resolve placeholders.
     * @param service the node containing properties which may appear as placeholders in other properties.
     */
    public void registerSource(ConfigService service) {
        sources.add(service);
    }

    /**
     * Given a property value, if the value contains placeholders, these will be replaced by the property values for
     * those placeholders.  Multiple and nested placeholders are also handled. If there are no placeholders in the
     * given value, it is returned as given.
     * <p>
     * Examples:
     * <pre>
     * http://${host}:${port}
     * ${name-${id}}
     * </pre>
     * </p>
     * @param propertyValue the value for the property (which may include placeholders)
     * @return the resolved value for the property
     * @throws UnresolvedPlaceholderException if the placeholder cannot be found in the registered property sources
     */
    public String resolve(String propertyValue, ConfigNode currentNode) {
        return replacePlaceholders(propertyValue, currentNode);
    }

    private String replacePlaceholders(String value, ConfigNode currentNode) {
        Pattern regEx = Pattern.compile("[^$]*\\$\\{([^$]+?)\\}.*");
        Matcher matcher = regEx.matcher(value);
        while (matcher.find()) {
            String placeholder = matcher.group(1);
            String placeholderValue = findProperty(placeholder, currentNode);
            value = value.replace("${" + placeholder + "}", placeholderValue);
            matcher = regEx.matcher(value);
        }
        return value;
    }

    private String findProperty(String name, ConfigNode currentNode) {
        Property property = currentNode.property(name);
        if (property != null) {
            return property.asString();
        } else {
            for (ConfigService source : sources) {
                Property value = source.root().property(name);
                if (value != null) {
                    return value.asString();
                }
            }
        }
        throw new UnresolvedPlaceholderException(name);
    }
}

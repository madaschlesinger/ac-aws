package com.adaptive.cloud.config.file;

import com.adaptive.cloud.config.*;
import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A means of resolving placeholders within a property value by looking within other configuration services.
 * <p>
 * The resolution strategy is as follows:
 * <ol>
 *     <li>Look in the same node as the property whose placeholders are being resolved</li>
 *     <li>Look in the registered configuration services, in the order they were registered</li>
 * </ol>
 * </p>
 * @author Spencer Ward
 */
public class PlaceholderResolver {
    public static final int MAXIMUM_STACK_DEPTH = 10;
    private Collection<ConfigService> sources = new ArrayList<>();

    /**
     * Adds a source of properties which can be used to resolve placeholders.
     * @param service the node containing properties which may appear as placeholders in other properties.
     */
    public void registerSource(ConfigService service) {
        sources.add(service);
    }

    /**
     * Given a property propertyValue, if the propertyValue contains placeholders, these will be replaced by the property values for
     * those placeholders.  Multiple and nested placeholders are also handled. If there are no placeholders in the
     * given propertyValue, it is returned as given.
     * <p>
     * Examples:
     * <pre>
     * http://${host}:${port}
     * ${name-${id}}
     * </pre>
     * </p>
     * @param propertyValue the propertyValue for the property (which may include placeholders)
     * @return the resolved propertyValue for the property
     * @throws UnresolvedPlaceholderException if the placeholder cannot be found in the registered property sources
     */
    public String resolve(String propertyValue, ConfigNode currentNode) {
        return new Resolution(0, propertyValue, currentNode).evaluate();
    }

    private class Resolution {
        private final String propertyValue;
        private final ConfigNode currentNode;
        private final int stackDepth;

        private Resolution(int stackDepth, String propertyValue, ConfigNode currentNode) {
            if (stackDepth > MAXIMUM_STACK_DEPTH) throw new CircularPlaceholderException(propertyValue);
            this.stackDepth = stackDepth;
            this.propertyValue = propertyValue;
            this.currentNode = currentNode;
        }

        public String evaluate() {
            Pattern regEx = Pattern.compile("[^$]*\\$\\{([^$]+?)\\}.*");
            Matcher matcher = regEx.matcher(propertyValue);
            String resolvedValue = propertyValue;
            while (matcher.find()) {
                String placeholder = matcher.group(1);
                String placeholderValue = findProperty(placeholder);
                resolvedValue = resolvedValue.replace("${" + placeholder + "}", placeholderValue);
                matcher = regEx.matcher(resolvedValue);
            }
            return resolvedValue;
        }

        private String findProperty(String name) {
            Property property = findInCurrentNode(name);
            if (property == null) {
                property = findInHierarchy(name);
            }

            String rawValue = property.rawValue();
            return new Resolution(stackDepth + 1, rawValue, currentNode).evaluate();
        }

        private Property findInCurrentNode(String name) {
            return currentNode.property(name);
        }

        private Property findInHierarchy(String name) {
            for (ConfigService service : sources) {
                Property value = findInService(service, name);
                if (value != null) {
                    return value;
                }
            }
            throw new UnresolvedPlaceholderException(name);
        }

        public Property findInService(ConfigService service, String path) {
            ConfigNode node = service.root();
            String element = "";

            Iterator<String> elements = Splitter.on(".").trimResults().split(path).iterator();
            while (elements.hasNext()) {
                element = elements.next();
                if (elements.hasNext()) {
                    node = node.child(element);
                    if (node == null) {
                        return null;
                    }
                }
            }
            return node.property(element);
        }
    }
}

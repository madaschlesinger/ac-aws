package com.adaptive.cloud.config.file;

import com.adaptive.cloud.config.ConfigNode;
import com.adaptive.cloud.config.Property;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a node of the configuration hierarchy.
 * <p>
 * Offers support for resolving placeholders in the property values. See {@link PlaceholderResolver}.
 * </p>
 * @author Spencer Ward
 */
class ConfigNodeImpl implements ConfigNode {
    private final PlaceholderResolver resolver;
    private final String name;
    private final ConfigNode parent;
    private final Map<String, Property> properties;
    private final Map<String, ConfigNode> children;

    private ConfigNodeImpl(String name, ConfigNode parent, PlaceholderResolver resolver) {
        this.name = name;
        this.parent = parent;
        this.resolver = resolver;
        this.properties = new HashMap<>();
        this.children = new HashMap<>();
        resolver.registerSource(this);
    }

    /**
     * Create a root config node with neither children nor properties.
     * @param resolver the mechanism for resolving properties.  The resolver will also be made aware of this config node to
     *                 allow other nodes to access this properties in this node
     * @return the root node of a config hierarchy
     */
    static ConfigNodeImpl createRoot(PlaceholderResolver resolver) {
        return new ConfigNodeImpl(null, null, resolver);
    }

    /**
     * Add a child node to the hierarchy
     * @param node the node name (this should be a DIRECT child, NOT a path to a descendant node)
     * @return the new child node
     */
    ConfigNodeImpl addChild(String node) {
        ConfigNodeImpl child = new ConfigNodeImpl(node, this, resolver);
        children.put(child.name(), child);
        return child;
    }

    /**
     * Add a property to this node.  The property value will be provided as a string, but may represent a different datatype.
     * @param name the name of the property
     * @param value the string representation of the property value
     */
    void addProperty(String name, String value) {
        properties.put(name, new StringProperty(name, value, resolver));
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public ConfigNode parent() {
        return parent;
    }

    @Override
    public Collection<ConfigNode> children() {
        return children.values();
    }

    @Override
    public ConfigNode child(String name) {
        return children.get(name);
    }

    @Override
    public Collection<Property> properties() {
        return properties.values();
    }

    @Override
    public Property property(String name) {
        return properties.get(name);
    }
}

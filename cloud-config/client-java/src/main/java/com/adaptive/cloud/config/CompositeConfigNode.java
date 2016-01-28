package com.adaptive.cloud.config;

import com.google.common.base.Function;
import com.google.common.collect.*;

import java.util.*;

/**
 * Implementation of {@link ConfigNode} that aggregates several nodes into one.  These nodes should have
 * the same name.
 * <p>
 * Root nodes might be merged to compose data from multiple sources, and other nodes in the hierarchy
 * might be merged to allow property augmentation and overriding.
 * </p>
 * <p>
 * Note that the order nodes are added is important. Properties and child nodes will be resolved by the first instance they're found in.
 * </p>
 *
 * @author Spencer Ward
 */

class CompositeConfigNode implements ConfigNode {
    private final ConfigNode parent;
    private final List<ConfigNode> nodes;

    private CompositeConfigNode(ConfigNode parent, List<ConfigNode> nodes) {
        this.parent = parent;
        this.nodes = nodes;
    }

    static CompositeConfigNode createRoot() {
        return new CompositeConfigNode(null, new ArrayList<ConfigNode>());
    }

    private CompositeConfigNode createChild(List<ConfigNode> nodes) {
        return new CompositeConfigNode(this, nodes);
    }

    @Override
    public String name() {
        return nodes.size() == 0 ? null : nodes.get(0).name();
    }

    @Override
    public ConfigNode parent() {
        return parent;
    }

    @Override
    public Collection<ConfigNode> children() {
        return childrenByName().values();
    }

    @Override
    public ConfigNode child(String name) {
        return childrenByName().get(name);
    }

    @Override
    public Collection<Property> properties() {
        return propertiesByName().values();
    }

    @Override
    public Property property(String name) {
        return propertiesByName().get(name);
    }

    protected void merge(ConfigNode node) {
        nodes.add(node);
    }

    private Map<String, ConfigNode> childrenByName() {
        Multimap<String, ConfigNode> children = ArrayListMultimap.create();
        for (ConfigNode node : nodes) {
            for (ConfigNode child : node.children()) {
                children.put(child.name(), child);
            }
        }

        return Maps.transformValues(children.asMap(), new Function<Collection<ConfigNode>, ConfigNode>() {
            public ConfigNode apply(Collection<ConfigNode> nodes) {
                return createChild(new ArrayList<>(nodes));
            }
        });
    }

    private Map<String, Property> propertiesByName() {
        Map<String, Property> properties = new HashMap<>();
        for (ConfigNode node : Lists.reverse(nodes)) {
            for (Property property : node.properties()) {
                properties.put(property.name(), property);
            }
        }
        return properties;
    }
}

package com.adaptive.cloud.config.composite;

import com.adaptive.cloud.config.ConfigNode;
import com.adaptive.cloud.config.Property;
import com.adaptive.cloud.config.file.PlaceholderResolver;
import com.adaptive.cloud.config.file.StringProperty;
import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

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
    private final PlaceholderResolver resolver;

    private CompositeConfigNode(ConfigNode parent, List<ConfigNode> nodes, PlaceholderResolver resolver) {
        this.parent = parent;
        this.nodes = nodes;
        this.resolver = resolver;
    }

    static CompositeConfigNode createRoot(PlaceholderResolver resolver) {
        return new CompositeConfigNode(null, new ArrayList<ConfigNode>(), resolver);
    }

    private CompositeConfigNode createChild(List<ConfigNode> nodes) {
        return new CompositeConfigNode(this, nodes, resolver);
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
        Multimap<String, ConfigNode> children = ArrayListMultimap.create();
        for (ConfigNode node : nodes) {
            for (ConfigNode child : node.children()) {
                children.put(child.name(), child);
            }
        }

        return Maps.transformValues(children.asMap(), new Function<Collection<ConfigNode>, ConfigNode>() {
            public ConfigNode apply(Collection<ConfigNode> nodes1) {
                return createChild(new ArrayList<>(nodes1));
            }
        }).values();
    }

    @Override
    public ConfigNode child(String name) {
        List<ConfigNode> children = new ArrayList<>();
        for (ConfigNode node : nodes) {
            ConfigNode child = node.child(name);
            if (child != null) {
                children.add(child);
            }
        }

        return createChild(children);
    }

    @Override
    public Collection<Property> properties() {
        Map<String, Property> properties = new HashMap<>();
        for (ConfigNode node : Lists.reverse(nodes)) {
            for (Property property : node.properties()) {
                StringProperty compositeProperty = new StringProperty(property.name(), property.rawValue(), this, resolver);
                properties.put(property.name(), compositeProperty);
            }
        }
        return properties.values();
    }

    @Override
    public Property property(String name) {
        for (ConfigNode node : nodes) {
            Property property = node.property(name);
            if (property != null) {
                return new StringProperty(property.name(), property.rawValue(), this, resolver);
            }
        }
        return null;
    }

    protected void merge(ConfigNode node) {
        nodes.add(node);
    }

}

package com.adaptive.cloud.config;

import com.google.common.base.Function;
import com.google.common.collect.*;

import java.util.*;

/**
 * Implementation of {@link ConfigNode} that aggregates several nodes into one.
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

class CompositeNode implements ConfigNode {
    private List<ConfigNode> nodes = new ArrayList<>();

    CompositeNode() {
    }

    CompositeNode(Collection<ConfigNode> configNodes) {
        nodes.addAll(configNodes);
    }

    void add(ConfigNode node) {
        nodes.add(node);
    }

    @Override
    public String name() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConfigNode parent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<ConfigNode> children() {
        return childrenByName().values();
    }

    @Override
    public ConfigNode child(String name) {
        return childrenByName().get(name);
    }

    private Map<String, ConfigNode> childrenByName() {
        Multimap<String, ConfigNode> children = ArrayListMultimap.create();
        for (ConfigNode node : nodes) {
            for (ConfigNode child : node.children()) {
                children.put(child.name(), child);
            }
        }
        
        return Maps.transformValues(children.asMap(), new Function<Collection<ConfigNode>, ConfigNode>() {
            public ConfigNode apply(Collection<ConfigNode> configNodes) {
                return configNodes.size() == 1 ? configNodes.iterator().next() : new CompositeNode(configNodes);
            }
        });
    }

    @Override
    public Collection<Property> properties() {
        List<Property> properties = new ArrayList<>();
        for (ConfigNode node : nodes) {
            properties.addAll(node.properties());
        }
        return properties;
    }

    @Override
    public Property property(String name) {
        throw new UnsupportedOperationException();
    }
}

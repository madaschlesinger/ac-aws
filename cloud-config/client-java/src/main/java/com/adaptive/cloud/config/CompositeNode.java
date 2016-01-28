package com.adaptive.cloud.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of {@link ConfigNode} that aggregates several nodes into one.
 * <p>
 * Root nodes might be merged to compose data from multiple sources, and other nodes in the heirarchy
 * might be merged to provide property augmentation and overriding facilities.
 * </p>
 * <h1>Example Usage</h1>
 * <p>
 * Note that the order nodes are added is important. Properties and child nodes will be resolved by the first instance they're found in.
 * </p>
 *
 * @author Spencer Ward
 */

class CompositeNode implements ConfigNode {
    private List<ConfigNode> nodes = new ArrayList<>();
    
    public void add(ConfigNode node) {
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
        List<ConfigNode> children = new ArrayList<>();
        for (ConfigNode node : nodes) {
            children.addAll(node.children());
        }
        return children;
    }

    @Override
    public ConfigNode child(String name) {
        throw new UnsupportedOperationException();
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

package com.adaptive.cloud.config;

import java.util.Collection;

/**
 * Interface representing a node of the configuration hierarchy.
 * <p>
 * This can be thought of as a logical collection of configuration properties, typically key/value pairs. Nodes may also
 * be organised hierarchically, with a node containing child nodes. Support for this feature depends on the configuration
 * service being used.
 * </p>
 * @author Kevin Seal
 */
public interface ConfigNode {

	/**
	 * Returns the name of this configuration node.
	 * <p>
	 * This name is relative to its parent node not a fully-qualified name for the node.
	 * </p>
	 */
	String name();

	/**
	 * Returns the parent node for this configuration node, or {@code null} if this is the root node.
	 */
	ConfigNode parent();

	/**
	 * Returns a collection of all the child nodes of this node. Will return an empty collection if there are no child nodes.
	 */
	Collection<ConfigNode> children();

	/**
	 * Returns the child node with the given name, or {@code null} if no such child node exists.
	 */
	ConfigNode child(String name);


	/**
	 * Returns a collection of the properties contained within this node.
	 */
	Collection<Property> properties();

	/**
	 * Returns the polymorphic property with the specified name, or {@code null} if no such property exists.
	 * <p>
	 * Note that this <em>won't</em> traverse the config hierarchy to find properties in child nodes, even if the name
	 * of the requested property <em>suggests</em> that the property is in a child node, eg. {@code database.username}.
	 * </p>
	 * TODO - Perhaps this SHOULD traverse hierarchy.  Especially if it is the root node.
	 */
	Property property(String name);
}

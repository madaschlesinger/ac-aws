package com.adaptive.cloud.config;

import com.adaptive.cloud.config.composite.CompositeConfigService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class CompositeConfigServiceTest {

    private CompositeConfigService composite;

    private ConfigService mockService() {
        ConfigService service = mock(ConfigService.class);
        ConfigNode root = mock(ConfigNode.class);
        when(service.root()).thenReturn(root);
        when(service.isOpen()).thenReturn(true);
        when(root.children()).thenReturn(Collections.<ConfigNode>emptyList());
        return service;
    }

    private ConfigService mockService(String nodeName, String propertyName, String propertyValue) {
        ConfigService service = mockService();
        ConfigNode node = addNode(service, nodeName);
        addProperty(node, propertyName, propertyValue);
        return service;
    }

    private ConfigService mockService(String nodeName, String property1Name, String property1Value, String property2Name, String property2Value) {
        ConfigService service = mockService(nodeName, property1Name, property1Value);
        addProperty(service.root().child(nodeName), property2Name, property2Value);
        return service;
    }

    private ConfigNode addNode(ConfigService service, String nodeName) {
        ArrayList<ConfigNode> children = new ArrayList<>(service.root().children());
        ConfigNode newNode = mockNode(nodeName);
        children.add(newNode);
        when(service.root().children()).thenReturn(children);
        when(service.root().child(nodeName)).thenReturn(newNode);
        return newNode;
    }

    private ConfigNode mockNode(String nodeName) {
        ConfigNode node = mock(ConfigNode.class);
        when(node.name()).thenReturn(nodeName);
        return node;
    }

    private void addProperty(ConfigNode node, String propertyName, String propertyValue) {
        ArrayList<Property> properties = new ArrayList<>(node.properties());
        Property property = mockProperty(propertyName, propertyValue);
        properties.add(property);
        when(node.property(propertyName)).thenReturn(property);
        when(node.properties()).thenReturn(properties);
    }

    private Property mockProperty(String propertyName, String propertyValue) {
        Property property = mock(Property.class);
        when(property.name()).thenReturn(propertyName);
        when(property.rawValue()).thenReturn(propertyValue);
        return property;
    }

    @Test
    public void itShould_InitiallyHaveAnEmptyRoot() throws Exception {
        composite = CompositeConfigService.of();
        assertThat(composite.root().children(), is(empty()));
        assertThat(composite.root().properties(), is(empty()));
    }

    @Test
    public void itShould_HaveAParentlessRoot() throws Exception {
        composite = CompositeConfigService.of();
        assertThat(composite.root().parent(), is(nullValue()));
    }

    @Test
    public void itShould_HaveAnUnnamedRoot() throws Exception {
        composite = CompositeConfigService.of();
        assertThat(composite.root().name(), is(nullValue()));
    }

    @Test
    public void itShould_HaveTheChildrenOfAddedService() throws Exception {
        ConfigService service1 = mockService("a node", "a property", "a value");
        composite = CompositeConfigService.of(service1);
        assertThat(composite.root().children().size(), is(1));
        assertThat(composite.root().child("a node").name(), is("a node"));
    }

    @Test
    public void itShould_HaveTheChildrenOfAllAddedServices() throws Exception {
        ConfigService service1 = mockService("node1", "property1", "value1");
        ConfigService service2 = mockService("node2", "property2", "value2");
        composite = CompositeConfigService.of(service1, service2);
        assertThat(composite.root().children().size(), is(2));
        assertThat(composite.root().child("node1").name(), is("node1"));
        assertThat(composite.root().child("node2").name(), is("node2"));
    }

    @Test
    public void itShould_ParentTheChildNodesOnTheCompositeRoot() throws Exception {
        ConfigService service1 = mockService("node1", "property1", "value1");
        ConfigService service2 = mockService("node2", "property2", "value2");
        composite = CompositeConfigService.of(service1, service2);
        composite.open();
        assertThat(composite.root().child("node1").parent(), is(composite.root()));
        assertThat(composite.root().child("node2").parent(), is(composite.root()));
    }

    @Test
    public void itShould_MergeNodesWithMatchingNames() throws Exception {
        ConfigService service1 = mockService("a node", "property1", "value1");
        ConfigService service2 = mockService("a node", "property2", "value2");
        composite = CompositeConfigService.of(service1, service2);
        assertThat(composite.root().children().size(), is(1));
        assertThat(composite.root().child("a node").properties().size(), is(2));
    }

    @Test
    public void itShould_ProvideAccessToPropertiesOfComposedServices() throws Exception {
        ConfigService service1 = mockService("node1", "property1", "value1");
        ConfigService service2 = mockService("node2", "property2", "value2");
        composite = CompositeConfigService.of(service1, service2);
        assertThat(composite.root().child("node1").property("property1").asString(), is("value1"));
        assertThat(composite.root().child("node2").property("property2").asString(), is("value2"));
    }

    @Test
    public void itShould_MergePropertiesWherePathsMatch() throws Exception {
        ConfigService service1 = mockService("a node", "a property", "value1");
        ConfigService service2 = mockService("a node", "a property", "value2");

        composite = CompositeConfigService.of(service1, service2);
        assertThat(composite.root().child("a node").properties().size(), is(1));
    }

    @Test
    public void itShould_FavourTheFirstServiceWherePropertyPathsMatch() throws Exception {
        ConfigService service1 = mockService("a node", "a property", "first Service value");
        ConfigService service2 = mockService("a node", "a property", "second Service value");

        composite = CompositeConfigService.of(service1, service2);
        assertThat(composite.root().child("a node").property("a property").asString(), is("first Service value"));
    }

    @Test
    public void itShould_ReturnNullForUnknownProperties() throws Exception {
        ConfigService service1 = mockService("a node", "a property", "a value");
        composite = CompositeConfigService.of(service1);
        assertThat(composite.root().child("a node").property("unknown"), is(nullValue()));
    }

    @Test
    public void itShould_ResolvePlaceholdersFromTheSameNode() throws Exception {
        ConfigService service1 = mockService("a node", "property1", "value1", "property2", "${property1}");
        composite = CompositeConfigService.of(service1);
        assertThat(composite.root().child("a node").property("property2").asString(), is("value1"));
    }

    @Test
    public void itShould_ResolvePlaceholdersFromTheSameNodeUsingFullyQualifiedPath() throws Exception {
        ConfigService service1 = mockService("a node", "property1", "value1", "property2", "${a node.property1}");
        composite = CompositeConfigService.of(service1);
        assertThat(composite.root().child("a node").property("property2").asString(), is("value1"));
    }

    @Test
    public void itShould_ResolvePlaceholdersFromADifferentNode() throws Exception {
        ConfigService service1 = mockService("node1", "property1", "value1");
        ConfigNode node2 = addNode(service1, "node2");
        addProperty(node2, "property2", "${node1.property1}");
        composite = CompositeConfigService.of(service1);
        assertThat(composite.root().child("node2").property("property2").asString(), is("value1"));
    }

    @Test
    public void itShould_ResolvePlaceholdersFromOtherServices() throws Exception {
        ConfigService service1 = mockService("node1", "property1", "value1");
        ConfigService service2 = mockService("node2", "property2", "${node1.property1}");
        composite = CompositeConfigService.of(service1, service2);
        assertThat(composite.root().child("node2").property("property2").asString(), is("value1"));
    }

    @Test
    public void itShould_ResolvePlaceholdersFromTheCurrentNodeInPreferenceToOtherNodes() throws Exception {
        ConfigService service1 = mockService("a node", "test.property1", "valueCurrentNode", "property2", "${test.property1}");
        ConfigNode node2 = addNode(service1, "test");
        addProperty(node2, "property1", "valueCurrentDifferentNode");
        composite = CompositeConfigService.of(service1);
        assertThat(composite.root().child("a node").property("property2").asString(), is("valueCurrentNode"));
    }

    @Test
    public void itShould_ResolvePlaceholdersFromTheFirstRegisteredService() throws Exception {
        ConfigService service1 = mockService("node1", "property1", "valueFirstService");
        ConfigService service2 = mockService("node1", "property1", "valueSecondService");
        ConfigService service3 = mockService("node2", "property2", "${node1.property1}");
        composite = CompositeConfigService.of(service1, service2, service3);
        assertThat(composite.root().child("node2").property("property2").asString(), is("valueFirstService"));
    }

    @Test
    public void itShould_GetPropertyWithNestedPlaceholders() throws Exception {
        ConfigService service1 = mockService("node1", "property1", "value1", "property2", "1");
        ConfigService service2 = mockService("node2", "property3", "${node1.property${node1.property2}}");
        composite = CompositeConfigService.of(service1, service2);
        assertThat(composite.root().child("node2").property("property3").asString(), is("value1"));
    }

    @Test(expected=UnresolvedPlaceholderException.class)
    public void itShould_ThrowExceptionWhenPlaceholderNotKnown() throws Exception {
        ConfigService service1 = mockService("a node", "a property", "${unknown}");
        composite = CompositeConfigService.of(service1);
        composite.root().child("a node").property("a property").asString();
    }

    @Test(expected=ConfigServiceClosedException.class)
    public void itShould_ThrowAnExceptionIfAnUndelyingSeviceIsClosed() throws Exception {
        ConfigService service1 = mockService("a node", "a property", "${unknown}");
        composite = CompositeConfigService.of(service1);
        when(service1.isOpen()).thenReturn(false);
        composite.root();
    }

    @Test
    public void itShould_OpenAllTheComposedServices() throws Exception {
        ConfigService service1 = mockService("a node", "a property", "value1");
        ConfigService service2 = mockService("a node", "a property", "value2");
        composite = CompositeConfigService.of(service1, service2);
        composite.open();
        verify(service1, times(1)).open();
        verify(service2, times(1)).open();
    }

    @Test
    public void itShould_CloseAllTheComposedServices() throws Exception {
        ConfigService service1 = mockService("a node", "a property", "value1");
        ConfigService service2 = mockService("a node", "a property", "value2");
        composite = CompositeConfigService.of(service1, service2);
        composite.close();
        verify(service1, times(1)).close();
        verify(service2, times(1)).close();
    }
}

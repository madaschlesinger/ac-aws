package com.adaptive.cloud.config;

import com.adaptive.cloud.config.composite.CompositeConfigService;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class CompositeConfigServiceTest {

    private CompositeConfigService composite;


    private ConfigService mockService(String nodeName, String propertyName, String propertyValue) {
        ConfigService service = mockService();
        addNode(service, nodeName, propertyName, propertyValue);
        return service;
    }

    private ConfigService mockService(String nodeName, String property1Name, String property1Value, String property2Name, String property2Value) {
        ConfigService service = mockService();
        addNode(service, nodeName, property1Name, property1Value);
        addNode(service, nodeName, property2Name, property2Value);
        return service;
    }

    private ConfigService mockService() {
        ConfigService service = mock(ConfigService.class);
        ConfigNode root = mock(ConfigNode.class);

        when(service.root()).thenReturn(root);
        when(service.isOpen()).thenReturn(true);
        when(root.children()).thenReturn(Collections.<ConfigNode>emptyList());

        return service;
    }

    private void addNode(ConfigService service, String nodeName, String propertyName, String propertyValue) {
        ArrayList<ConfigNode> children = new ArrayList<>(service.root().children());
        ConfigNode newNode = mockNode(nodeName, ImmutableMap.of(propertyName, propertyValue));
        children.add(newNode);
        when(service.root().children()).thenReturn(children);
        when(service.root().child(nodeName)).thenReturn(newNode);
    }

    private ConfigNode mockNode(String nodeName, Map<String, String> propertyNamesAndValues) {
        ConfigNode node = mock(ConfigNode.class);
        when(node.name()).thenReturn(nodeName);

        ArrayList<Property> properties = new ArrayList<>();
        for (Map.Entry<String, String> propertyNameAndValue : propertyNamesAndValues.entrySet()) {
            Property property = mock(Property.class);
            properties.add(property);
            when(node.property(propertyNameAndValue.getKey())).thenReturn(property);
            when(property.name()).thenReturn(propertyNameAndValue.getKey());
            when(property.rawValue()).thenReturn(propertyNameAndValue.getValue());
        }
        when(node.properties()).thenReturn(properties);
        return node;
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
        ConfigService service1 = mockService("a node", "property1", "value1", "property2", "${a node.property1}");
        composite = CompositeConfigService.of(service1);
        assertThat(composite.root().child("a node").property("property2").asString(), is("value1"));
    }

    @Test
    public void itShould_ResolvePlaceholdersADifferentNode() throws Exception {
        ConfigService service1 = mockService("node1", "property1", "value1");
        addNode(service1, "node2", "property2", "${node1.property1}");
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

//    @Test
//    public void itShould_ResolvePlaceholdersFromTheCurrentNodeIfPossible() throws Exception {
//        ConfigService service1 = mockService("a node", "a property", "value1");
//        ConfigService service2 = mockService("a node", "a property", "value2");
//
//        composite = CompositeConfigService.of(service1, service2);
//        assertThat(composite.root().child("node2").property("property2").asString(), is("valueCurrentNode"));
//    }

//    @Test
//    public void itShould_ResolvePlaceholdersFromTheFirstRegisteredService() throws Exception {
//        when(property1.rawValue()).thenReturn("valueOtherServiceNode");
//        when(property2.rawValue()).thenReturn("${node1.property1}");
//
//        Property property3 = mock(Property.class);
//        when(node2.properties()).thenReturn(ImmutableList.of(property2, property3));
//        when(node2.property("property3")).thenReturn(property3);
//        when(property3.name()).thenReturn("node1.property1");
//        when(property3.rawValue()).thenReturn("valueCurrentNode");
//
//        composite = CompositeConfigService.of(service1, service2);
//        assertThat(composite.root().child("node2").property("property2").asString(), is("valueCurrentNode"));
//    }

//    @Test
//    public void itShould_GetPropertyWithNestedPlaceholders() throws Exception {
//        Property property3 = mock(Property.class);
//        when(node1.properties()).thenReturn(ImmutableList.of(property1, property3));
//        when(node1.property("property3")).thenReturn(property3);
//        when(property3.name()).thenReturn("property3");
//        when(property3.rawValue()).thenReturn("1");
//
//        when(property1.rawValue()).thenReturn("value1");
//        when(property2.rawValue()).thenReturn("${node1.property${node1.property3}}");
//        composite = CompositeConfigService.of(service1, service2);
//
//        assertThat(composite.root().child("node2").property("property2").asString(), is("value1"));
//    }

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

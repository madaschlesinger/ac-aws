package com.adaptive.cloud.config;

import com.adaptive.cloud.config.composite.CompositeConfigService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompositeConfigServiceTest {
    @Mock private ConfigService service1;
    @Mock private ConfigService service2;
    @Mock private ConfigNode root1;
    @Mock private ConfigNode root2;
    @Mock private ConfigNode node1;
    @Mock private ConfigNode node2;
    @Mock private Property property1;
    @Mock private Property property2;
    private CompositeConfigService composite;

    @Before
    public void before() {
        when(service1.root()).thenReturn(root1);
        when(service2.root()).thenReturn(root2);
        when(root1.children()).thenReturn(Collections.singleton(node1));
        when(root2.children()).thenReturn(Collections.singleton(node2));
        when(node1.name()).thenReturn("node1");
        when(node2.name()).thenReturn("node2");
        when(node1.properties()).thenReturn(Collections.singleton(property1));
        when(node2.properties()).thenReturn(Collections.singleton(property2));
        when(node1.property("property1")).thenReturn(property1);
        when(node2.property("property2")).thenReturn(property2);
        when(property1.name()).thenReturn("property1");
        when(property2.name()).thenReturn("property2");
//        when(property1.asString()).thenReturn("value1");
//        when(property2.asString()).thenReturn("value2");
        when(property1.rawValue()).thenReturn("value1");
        when(property2.rawValue()).thenReturn("value2");
    }

    @Test
    public void itShould_InitiallyHaveAnEmptyRoot() {
        composite = CompositeConfigService.of();
        assertThat(composite.root().children(), is(empty()));
        assertThat(composite.root().properties(), is(empty()));
    }

    @Test
    public void itShould_HaveAParentlessRoot() {
        composite = CompositeConfigService.of();
        assertThat(composite.root().parent(), is(nullValue()));
    }

    @Test
    public void itShould_HaveAnUnnamedRoot() {
        composite = CompositeConfigService.of();
        assertThat(composite.root().name(), is(nullValue()));
    }

    @Test
    public void itShould_HaveTheChildrenOfAddedSource() {
        composite = CompositeConfigService.of(service1);
        assertThat(composite.root().children().size(), is(1));
        assertThat(composite.root().child("node1").name(), is("node1"));
    }

    @Test
    public void itShould_HaveTheChildrenOfAllAddedSources() {
        composite = CompositeConfigService.of(service1, service2);
        assertThat(composite.root().children().size(), is(2));
        assertThat(composite.root().child("node1").name(), is("node1"));
        assertThat(composite.root().child("node2").name(), is("node2"));
    }

    @Test
    public void itShould_ParentTheChildNodesOnTheCompositeRoot() {
        composite = CompositeConfigService.of(service1, service2);
        assertThat(composite.root().child("node1").parent(), is(composite.root()));
        assertThat(composite.root().child("node2").parent(), is(composite.root()));
    }

    @Test
    public void itShould_MergeNodesWithMatchingNames() {
        when(node1.name()).thenReturn("a node");
        when(node2.name()).thenReturn("a node");
        composite = CompositeConfigService.of(service1, service2);
        assertThat(composite.root().children().size(), is(1));
        assertThat(composite.root().child("a node").properties().size(), is(2));
    }

    @Test
    public void itShould_ProvideAccessToPropertiesOfComposedServices() {
        composite = CompositeConfigService.of(service1, service2);
        assertThat(composite.root().child("node1").property("property1").asString(), is("value1"));
        assertThat(composite.root().child("node2").property("property2").asString(), is("value2"));
    }

    @Test
    public void itShould_MergePropertiesWherePathsMatch() {
        when(node1.name()).thenReturn("a node");
        when(node2.name()).thenReturn("a node");
        when(node1.property("a property")).thenReturn(property1);
        when(node2.property("a property")).thenReturn(property2);
        when(property1.name()).thenReturn("a property");
        when(property2.name()).thenReturn("a property");

        composite = CompositeConfigService.of(service1, service2);
        assertThat(composite.root().child("a node").properties().size(), is(1));
    }

    @Test
    public void itShould_FavourTheFirstServiceWherePropertyPathsMatch() {
        when(node1.name()).thenReturn("a node");
        when(node2.name()).thenReturn("a node");
        when(node1.property("a property")).thenReturn(property1);
        when(node2.property("a property")).thenReturn(property2);
        when(property1.name()).thenReturn("a property");
        when(property2.name()).thenReturn("a property");
        when(property1.rawValue()).thenReturn("first source value");
        when(property2.rawValue()).thenReturn("second source value");

        composite = CompositeConfigService.of(service1, service2);
        assertThat(composite.root().child("a node").property("a property").asString(), is("first source value"));
    }

    @Test
    public void itShould_ReturnNullForUnknownProperties() {
        composite = CompositeConfigService.of(service1);
        assertThat(composite.root().child("node1").property("unknown"), is(nullValue()));
    }

    @Test
    public void itShould_GetPropertyWithPlaceholders() throws Exception {
        when(property1.rawValue()).thenReturn("value1");
        when(property2.rawValue()).thenReturn("${node1.property1}");
        composite = CompositeConfigService.of(service1, service2);
        assertThat(composite.root().child("node2").property("property2").asString(), is("value1"));
    }

    @Test
    @Ignore
    public void itShould_GetPropertyWithNestedPlaceholders() throws Exception {
    }

    @Test(expected=UnresolvedPlaceholderException.class)
    public void itShould_ThrowExceptionWhenPlaceholderNotKnown() throws Exception {
        when(property1.rawValue()).thenReturn("${unknown}");
        composite = CompositeConfigService.of(service1);
        composite.root().child("node1").property("property1").asString();
    }

    @Test
    public void itShould_OpenAllTheComposedServices() throws Exception {
        composite = CompositeConfigService.of(service1, service2);
        composite.open();
        verify(service1, times(1)).open();
        verify(service2, times(1)).open();
    }

    @Test
    public void itShould_CloseAllTheComposedServices() throws Exception {
        composite = CompositeConfigService.of(service1, service2);
        composite.close();
        verify(service1, times(1)).close();
        verify(service2, times(1)).close();
    }

}

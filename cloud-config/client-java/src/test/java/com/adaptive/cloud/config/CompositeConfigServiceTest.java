package com.adaptive.cloud.config;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
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
        when(property1.name()).thenReturn("property1");
        when(property2.name()).thenReturn("property2");
    }

    @Test
    public void itShould_InitiallyHaveAnEmptyRoot() {
        composite = CompositeConfigService.of();
        assertThat(composite.root().children(), is(empty()));
        assertThat(composite.root().properties(), is(empty()));
    }

    @Test
    public void itShould_HaveTheChildrenOfAddedSource() {
        composite = CompositeConfigService.of(service1);
        assertThat(composite.root().children(), contains(node1));
        assertThat(composite.root().child("node1"), is(node1));
    }

    @Test
    public void itShould_HaveTheChildrenOfAllAddedSources() {
        composite = CompositeConfigService.of(service1, service2);
        assertThat(composite.root().children(), containsInAnyOrder(node1, node2));
        assertThat(composite.root().child("node1"), is(node1));
        assertThat(composite.root().child("node2"), is(node2));
    }

    @Test
    public void itShould_MergeNodesWithMatchingNames() {
        when(node1.name()).thenReturn("a node");
        when(node2.name()).thenReturn("a node");
        composite = CompositeConfigService.of(service1, service2);
        assertThat(composite.root().children().size(), is(1));
        assertThat(composite.root().child("a node").properties().size(), is(2));
    }
}

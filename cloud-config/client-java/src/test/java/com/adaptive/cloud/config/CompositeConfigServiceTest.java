package com.adaptive.cloud.config;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.hamcrest.Matchers.*;
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
    private CompositeConfigService composite;

    @Before
    public void before() {
        when(service1.root()).thenReturn(root1);
        when(service2.root()).thenReturn(root2);
        when(service1.root().children()).thenReturn(Collections.singleton(node1));
        when(service2.root().children()).thenReturn(Collections.singleton(node2));
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
    }

    @Test
    public void itShould_HaveTheChildrenOfAllAddedSources() {
        composite = CompositeConfigService.of(service1, service2);
        assertThat(composite.root().children(), contains(node1, node2));
    }
}

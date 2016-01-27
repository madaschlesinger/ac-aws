package com.adaptive.cloud.config;

import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FileConfigServiceTest {
    private ConfigNode root;

    @Before
    public void before() throws Exception {
        URL fileUrl = getClass().getClassLoader().getResource("test.properties");
        FileConfigService service = FileConfigService.fromProperties("node", fileUrl);
        root = service.root();
    }

    @Test
    public void itShould_GetPropertyByName() throws Exception {
        assertThat(root.child("node").property("host").asString(), is("foo"));
    }

    @Test
    public void itShould_GetIntegerProperty() throws Exception {
        assertThat(root.child("node").property("port").asInteger(), is(1234));
    }

    @Test(expected=InvalidConversionException.class)
    public void itShould_ThrowExceptionForNonIntegersAccessedAsInteger() throws Exception {
        root.child("node").property("host").asInteger();
    }

    @Test
    public void itShould_GetPropertyWithPlaceholders() throws Exception {
        assertThat(root.child("node").property("url").asString(), is("http://foo:1234"));
    }

    @Test
    public void itShould_GetPropertyWithNestedPlaceholders() throws Exception {
        assertThat(root.child("node").property("name").asString(), is("Jeff"));
    }

    @Test(expected=UnresolvedPlaceholderException.class)
    public void itShould_ThrowExceptionWhenPlaceholderNotKnown() throws Exception {
        root.child("node").property("unresolvable").asString();
    }
}


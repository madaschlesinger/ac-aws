package com.adaptive.cloud.config;

import com.adaptive.cloud.config.composite.CompositeConfigService;
import com.adaptive.cloud.config.file.FileConfigService;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class FileConfigServiceTest {
    private ConfigNode root;
    private URL fileUrl;

    @Before
    public void before() throws Exception {
        fileUrl = getClass().getClassLoader().getResource("test.properties");
        ConfigService service = FileConfigService.fromProperties("node", fileUrl);
        service.open();
        root = service.root();
    }

    @Test(expected=ConfigServiceClosedException.class)
    public void itShould_ThrowAnExceptionIfAccessedWithoutOpening() throws Exception {
        ConfigService service = FileConfigService.fromProperties("node", fileUrl);
        root = service.root();
    }

    @Test(expected=ConfigServiceClosedException.class)
    public void itShould_ThrowAnExceptionIfAccessedAfterClosing() throws Exception {
        ConfigService service = FileConfigService.fromProperties("node", fileUrl);
        service.open();
        service.close();
        root = service.root();
    }

    @Test
    public void itShould_ReturnNullForUnknownProperties() throws Exception {
        assertThat(root.child("node").property("unknown"), is(nullValue()));
    }

    @Test
    public void itShould_GetPropertyByName() throws Exception {
        assertThat(root.child("node").property("host").asString(), is("foo"));
    }

    @Test
    public void itShould_GetIntegerProperty() throws Exception {
        assertThat(root.child("node").property("port").asInteger(), is(1234));
    }

    @Test
    public void itShould_ReturnNullForGetPropertiesByUnknownPath() throws Exception {
        assertThat(root.property("unknown.property"), is(nullValue()));
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

    @Test(expected=CircularPlaceholderException.class)
    public void itShould_ThrowExceptionWhenPlaceholderCreateInfiniteRecursion() throws Exception {
        root.child("node").property("circular").asString();
    }
}


package com.adaptive.cloud.config;

import com.adaptive.cloud.config.composite.CompositeConfigService;
import com.adaptive.cloud.config.file.FileConfigService;
import org.junit.Test;

import java.net.URL;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class IntegrationTest {
    @Test
    public void basicTest() throws Exception {
        ConfigService overrideFile = FileConfigService.fromProperties("database", url("integration-overrides.properties"));
        ConfigService mainFile = FileConfigService.fromProperties("main", url("integration-main.properties"));
        CompositeConfigService service = CompositeConfigService.of(overrideFile, mainFile);
        service.open();
        Property url = service.root().child("main").property("database.url");
        assertThat(url.asString(), is("jdbc:postgresql://localhost:1234/database?user=dev"));
    }

    private URL url(String filename) {
        return getClass().getClassLoader().getResource(filename);
    }

}

package com.adaptive.cloud.config.file;

import com.adaptive.cloud.config.ConfigNode;
import com.adaptive.cloud.config.ConfigService;
import com.adaptive.cloud.config.ConfigServiceClosedException;
import com.adaptive.cloud.config.Property;
import com.google.common.base.Splitter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A {@code ConfigService} implementation whose nodes are backed by a file.
 * <p>
 * Each file you want loaded is mapped to a single configuration node.  
 * </p>
 * <h1>Example Usage</h1>
 * <p>
 * FileConfigService config = FileConfigService.fromProperties("database", getClass().getResource("/database.properties");
 * </p> 
 * @author Kevin Seal
 * @author Spencer Ward
 */
public class FileConfigService implements ConfigService {
	private final ConfigNodeImpl root;
	private boolean isOpen = false;

	/**
	 * Creates a new instance of a configuration service whose data is loaded into a single from a file in Java Properties file format.
	 * @param node	The node to put the data in - cannot be {@code null}
	 * @param url	The URL of the file to load
	 */
	public static FileConfigService fromProperties(String node, URL url) throws IOException {
		FileConfigService service = new FileConfigService();
		ConfigNodeImpl configNode = service.root.addChild(node);
		addProperties(url, configNode);
		return service;
	}

	private FileConfigService() {
		PlaceholderResolver resolver = new PlaceholderResolver();
		resolver.registerSource(this);
		this.root = ConfigNodeImpl.createRoot(resolver);
	}

	private static void addProperties(URL url, ConfigNodeImpl configNode) throws IOException {
		try (InputStream stream = url.openStream()) {
			for (Map.Entry<Object, Object> entry : getProperties(stream)) {
				configNode.addProperty(entry.getKey().toString(), entry.getValue().toString());
			}
		}
	}

	private static Set<Map.Entry<Object, Object>> getProperties(InputStream inputStream) throws IOException {
		Properties properties = new Properties();
		properties.load(inputStream);
		return properties.entrySet();
	}

	// Could have fromXML, fromJSON etc.
	
	@Override
	public void close() throws Exception {
		// Nothing to close really
		isOpen = false;
	}

	@Override
	public void open() throws Exception {
		// Nothing to open - files were loaded when they were added
		isOpen = true;
	}

	@Override
	public boolean isOpen() {
		return isOpen;
	}

	@Override
	public ConfigNode root() {
		ensureOpen();
		return root;
	}

	private void ensureOpen() {
		if (!isOpen) throw new ConfigServiceClosedException();
	}


}

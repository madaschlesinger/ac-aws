package com.adaptive.cloud.config.composite;

import com.adaptive.cloud.config.ConfigNode;
import com.adaptive.cloud.config.ConfigService;
import com.adaptive.cloud.config.file.PlaceholderResolver;

import java.util.Arrays;
import java.util.List;

/**
 * Implementation of {@link ConfigService} that aggregates several sources into one.
 * <p>
 * This could be used, for example, to support legacy configuration sources such as database tables or embedded property
 * files while migrating to more strategic service implementations like ZooKeeper.
 * </p>
 * <h1>Example Usage</h1>
 * <p>
 * Simply instantiate an instance of this composite service and all the service instances to it that you want to aggregate.
 * Note that the order they are added is important. Properties and nodes will be resolved by the first instance they're found in.
 * </p>
 * 
 * <pre>
 * CompositeConfigService config = new CompositeConfigService();
 * config.merge(zkConfig);
 * config.merge(databaseConfig);
 * </pre>
 * @author Kevin Seal
 */
public class CompositeConfigService implements ConfigService {
	private List<ConfigService> services;
	private CompositeConfigNode root;

	public CompositeConfigService(ConfigService... services) {
		PlaceholderResolver resolver = new PlaceholderResolver();
		this.services = Arrays.asList(services);
		this.root = CompositeConfigNode.createRoot(resolver);
		for (ConfigService service : services) {
			root.merge(service.root());
		}
	}

	@Override
	public void close() throws Exception {
		for (ConfigService service : services) {
			service.close();
		}
	}

	@Override
	public void open() throws Exception {
		for (ConfigService service : services) {
			service.open();
		}
	}

	@Override
	public ConfigNode root() {
		return root;
	}

	/**
	 * Convenience method that creates a composite of the given configuration services.
	 * @param services The services to aggregate, in descending priority order
	 */
	public static CompositeConfigService of(ConfigService... services) {
		return new CompositeConfigService(services);
	}
}

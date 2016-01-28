package com.adaptive.cloud.config;

import java.util.ArrayList;
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
 * config.add(zkConfig);
 * config.add(databaseConfig);
 * </pre>
 * @author Kevin Seal
 */
public class CompositeConfigService implements ConfigService {
	private List<ConfigService> services = new ArrayList<>();

	@Override
	public void close() throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public void open() throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public ConfigNode root() {
		CompositeNode compositeRoot = new CompositeNode();
		for (ConfigService service : services) {
			compositeRoot.add(service.root());
		}
		return compositeRoot;
	}

	/**
	 * Convenience method that creates a composite of the given configuration services.
	 * @param configs The services to aggregate, in descending priority order
	 */
	public static CompositeConfigService of(ConfigService... configs) {
		CompositeConfigService composite = new CompositeConfigService();
		for (ConfigService config : configs) {
			composite.add(config);
		}
		return composite;
	}

	private void add(ConfigService config) {
		services.add(config);
	}
}

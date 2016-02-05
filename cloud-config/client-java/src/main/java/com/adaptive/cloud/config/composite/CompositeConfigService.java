package com.adaptive.cloud.config.composite;

import com.adaptive.cloud.config.ConfigNode;
import com.adaptive.cloud.config.ConfigService;
import com.adaptive.cloud.config.ConfigServiceClosedException;
import com.adaptive.cloud.config.Property;
import com.adaptive.cloud.config.file.PlaceholderResolver;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

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
 * @author Spencer Ward
 */
public class CompositeConfigService implements ConfigService {
	private final List<ConfigService> services;
	private CompositeConfigNode root;


	/**
	 * Factory method that creates a composite of the given configuration services.
	 * @param services The services to aggregate, in descending priority order
	 */
	public static CompositeConfigService of(ConfigService... services) {
		return new CompositeConfigService(services);
	}

	private CompositeConfigService(ConfigService... services) {
		this.services = Arrays.asList(services);
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
	public boolean isOpen() {
		return Iterables.all(services, new Predicate<ConfigService>() {
			public boolean apply(ConfigService configService) {
				return configService.isOpen();
			}
		});
	}

	@Override
	public ConfigNode root() {
		ensureOpen();
		if (root == null) {
			PlaceholderResolver resolver = new PlaceholderResolver();
			resolver.registerSource(this);
			this.root = CompositeConfigNode.createRoot(resolver);
			for (ConfigService service : services) {
				root.merge(service.root());
			}
		}
		return root;
	}

	private void ensureOpen() {
		if (!isOpen()) throw new ConfigServiceClosedException();
	}
}

package com.adaptive.cloud.config;

/**
 * Interface for a service providing access to configuration data.
 * <p>
 * This API is agnostic of the source of these configuration data. Indeed, an instance of this service may span multiple data sources
 * </p>
 * @author Kevin Seal
 */
public interface ConfigService extends AutoCloseable {

	/**
	 * Opens/starts this configuration service instance. 
	 * <p>
	 * It is expected that this is called prior to any attempt to use this instance. Behaviour of this service is undefined if this method is not called first or if it throws an exception.
	 * </p>
	 */
	void open() throws Exception;
	
	/**
	 * Returns the root node for this configuration service.
	 * <p>
	 * This is the entry point into the configuration hierarchy for this instance of the configuration service.
	 * </p>
	 */
	ConfigNode root();

	/**
	 * TODO
	 * @param path
	 * @return
     */
	Property property(String path);
}

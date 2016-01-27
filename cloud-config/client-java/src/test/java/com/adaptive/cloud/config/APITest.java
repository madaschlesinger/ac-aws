package com.adaptive.cloud.config;

import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * That's right, a unit test for an interface.
 * <p>
 * Perhaps this can be construed more as intended API usage - there is no implementation tested herein and nothing asserted. The fact that this compiles is enough.
 * </p>
 * @author Kevin Seal
 */
public class APITest {

	private ConfigService service;
	
	@Before
	public void setup() {
		// A stub service -- when there's a properties-based implemented it would go in here more concisely
		service = mock(ConfigService.class);
		ConfigNode root = mock(ConfigNode.class);
		Property property = mock(Property.class);
		
		when(service.root()).thenReturn(root);
		when(root.child(any(String.class))).thenReturn(root);
		when(root.property(any(String.class))).thenReturn(property);
	}
	
	@After
	public void tearDown() throws Exception {
		service.close();
	}
	
	/**
	 * This "test" imagines obtaining some primitive properties from a config node in a known location.
	 */
	@Test
	public void testPropertyMakesSense() {
		
		// Pretend our properties are stored on a "test" configuration node
		ConfigNode node = service.root().child("test");

		// Get hold of some properties of different types
		String user = node.property("username").asString();
		int port = node.property("port").asInteger();
		double slippage = node.property("slippage").asDouble();
	}

	/**
	 * This "test" imagines iterating over child nodes that each represent a connection we have to establish.
	 */
	@Test
	public void testHierarchyMakesSense() {
		// A parent node in which there is a node for each nirvana cluster
		ConfigNode nirvanas  = service.root().child("nirvana");

		// Iterate through all the clusters defined inside
		for(ConfigNode nirvana : nirvanas.children()) { 
			String name = nirvana.name();
			String rname = nirvana.property("rname").asString(); 
			String account = nirvana.property("account").asString();
		}
	}

}

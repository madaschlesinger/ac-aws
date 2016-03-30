package com.weareadaptive.spikes.services.indexed.rest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class IndexPoolTests {
    private static final String ADDRESS = "node1:2181";
    private static final String PATH = "/pool";

    @Test
    public void testAcquireIndex() throws Exception {
        IndexPool pool = new IndexPool(ADDRESS);
        pool.start();
        int index = pool.acquireIndex(PATH);
        assertTrue(index >=0 && index < IndexPool.NUM_ITEMS);
        pool.close();
    }

    @Test
    public void testCleanup() throws Exception {
        IndexPool pool = new IndexPool(ADDRESS);
        pool.start();
        pool.cleanUp(PATH);
        pool.close();
    }
}

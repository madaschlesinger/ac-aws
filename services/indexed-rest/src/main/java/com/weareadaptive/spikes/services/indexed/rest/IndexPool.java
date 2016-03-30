package com.weareadaptive.spikes.services.indexed.rest;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.CreateMode;

public class IndexPool implements AutoCloseable {
    private final CuratorFramework client;
    public static final int NUM_ITEMS = 2;
    private static final String MUTEX = "mutex";
    private static final String ITEMS = "items";

    public IndexPool(String address) throws Exception {
        client = CuratorFrameworkFactory.newClient(address, new RetryOneTime(1));
    }

    public void start() throws Exception {
        client.start();
    }

    @Override
    public void close() throws Exception {
        client.close();
    }

    public int acquireIndex(String path) throws Exception {
        int numItems;
        if (client.checkExists().forPath(path) == null) {
            numItems = NUM_ITEMS;
            byte[] numItemsBytes = Integer.toString(numItems).getBytes();
            client.create().withMode(CreateMode.PERSISTENT).forPath(path, numItemsBytes);
        } else {
            byte[] numItemsBytes = client.getData().forPath(path);
            numItems = Integer.parseInt(new String(numItemsBytes));

        }
        String mutexPath = String.format("%s/%s", path, MUTEX);
        InterProcessMutex mutex = new InterProcessMutex(client, mutexPath);
        mutex.acquire();
        try {
            String itemsPath = String.format("%s/%s", path, ITEMS);
            if (client.checkExists().forPath(itemsPath) == null) {
                client.create().withMode(CreateMode.PERSISTENT).forPath(itemsPath);
            }
            for (int i=0; i<numItems; ++i) {
                String itemPath = String.format("%s/%d", itemsPath, i);
                if (client.checkExists().forPath(itemPath) == null) {
                    client.create().withMode(CreateMode.EPHEMERAL).forPath(itemPath);
                    return i;
                }
            }
        } finally {
            mutex.release();
        }
        throw new Exception("Unable to acquire index");
    }

    public void cleanUp(String path) throws Exception {
        if (client.checkExists().forPath(path) != null) {
            client.delete().deletingChildrenIfNeeded().forPath(path);
        }
    }
}

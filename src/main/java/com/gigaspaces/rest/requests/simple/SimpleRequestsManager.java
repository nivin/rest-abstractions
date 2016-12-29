package com.gigaspaces.rest.requests.simple;

import com.gigaspaces.rest.requests.Request;
import com.gigaspaces.rest.requests.RequestStatus;
import com.gigaspaces.rest.requests.RequestsManager;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class SimpleRequestsManager extends RequestsManager {
    private final AtomicLong idGenerator = new AtomicLong(0);
    private final Map<Long, Request> requestsMap = new ConcurrentHashMap<Long, Request>();
    private final BlockingQueue<Request> requestQueue;
    private final Thread consumerThread = new Thread(new Consumer(), "GS-RequestsConsumer");

    public SimpleRequestsManager(int capacity) {
        requestQueue = new ArrayBlockingQueue<Request>(capacity);
        consumerThread.setDaemon(true);
        consumerThread.start();
    }

    @Override
    public void close() throws IOException {
        consumerThread.interrupt();
    }

    @Override
    public long submit(Request request) {
        request.setStatus(RequestStatus.PENDING);
        if (!requestQueue.offer(request)) {
            // TODO: What happens when queue capacity is breached?
        }
        final long id = idGenerator.incrementAndGet();
        requestsMap.put(id, request);
        return id;
    }

    @Override
    public Request get(long requestId) {
        return requestsMap.get(requestId);
    }

    @Override
    public Iterator<Request> getPendingRequests() {
        return requestQueue.iterator();
    }

    private class Consumer implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    final Request request = requestQueue.take();
                    request.process();
                    request.setStatus(RequestStatus.COMPLETED);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}

package com.gigaspaces.rest.requests;

import java.io.Closeable;
import java.util.Iterator;

/**
 * Abstraction for managing requests.
 *
 *
 */
public abstract class RequestsManager implements Closeable {
    /**
     * Submits a request
     * @param request Request to submit
     * @return generated request id.
     */
    public abstract long submit(Request request);

    /**
     * Retrieves a request by its id
     * @param requestId id of request to retrieve
     * @return The matching request, or null if no such request
     */
    public abstract Request get(long requestId);

    public abstract Iterator<Request> getPendingRequests();
}

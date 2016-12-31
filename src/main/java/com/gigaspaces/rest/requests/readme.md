# Overview

In modern APIs in general, and web/rest APIs in particular, it's recommended that potentially-long operations are performed in an async matter, i.e. the operation returns immediately, with some indicator which allows the client to track progress of the operation via a separate API call.

For our Admin REST API, we've decided that every such operation will be queued, and the queue will have a single consumer to process the requests. This is because processing multiple requests concurrently can cause unpredictable results, and because in its essence an Admin API is not about performance.

# API

The main classes are `Request` and `RequestsManager`. 
`Request` is an abstract class, which represents a request to perform an async operation. It has a status (`PENDING` or `COMPLETED`), and an abstract method `process()`.
Each async operation type will have a corresponding class (e.g. `DeployRequest`), which overrides `process` to perform the actual operation.

When te REST server handles an operation which should be processed asynchronously, it creates the corresponding `Request` instance with the relevant details, and submits it for async execution using the `RequestManager.submit(Request)` method. The submit method returns a requestId, which is propagated back to the client.
  
If the client wants to track a request, it executes `GET /requests/{id}`, which is implemented using `RequestsManager.get(requestId)`, and returns the matching request (if any). Requests which have already been processed are marked `COMPLETED`, but kept in `RequestManager` for additional period (configurable).
 
The client can also list all pending requests using `GET /requests`, which is implemented using `RequestsManager.getPendingRequests()`
  
## Processing Pending Requests

The `RequestsManager` is in charge of consuming pending requests according to the order in which they were submitted and executing their `process()` method. Success/failure and relevant details will be reflected in the `Request` state (properties).

## Evicting Completed Requests

Completed requests cannot be evicted right away (the client needs to be able to poll their state for a while), and cannot be kept indefinitely. The `RequestsManager` is in charge of evicting them after a while (TBD exact details).

# Default Implementation

The default implementation is `SimpleRequestsManager`. It uses a ConcurrentMap to store submitted requests, a BlockingQueue to keep track of pending requests, an AtomicLong to generate ids sequentially, and a consumer thread to poll the BlockingQueue and process requests.

See `RequestsDemo` to for example of how to use this.

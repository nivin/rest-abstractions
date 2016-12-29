package com.gigaspaces.rest.requests;

public abstract class Request {
    private RequestStatus status;

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public abstract void process();
}

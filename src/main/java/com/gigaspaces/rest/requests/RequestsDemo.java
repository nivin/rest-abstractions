package com.gigaspaces.rest.requests;

import com.gigaspaces.rest.requests.simple.SimpleRequestsManager;
import java.io.IOException;

public class RequestsDemo {

    public static void main(String[] args) throws IOException, InterruptedException {
        RequestsManager manager = new SimpleRequestsManager(10);
        manager.submit(new TestRequest().setData("Hello"));
        manager.submit(new TestRequest().setData("World"));
        Thread.sleep(1000);
        manager.close();
    }

    public static class TestRequest extends Request {

        private String data;


        public String getData() {
            return data;
        }

        public TestRequest setData(String data) {
            this.data = data;
            return this;
        }

        @Override
        public void process() {
            System.out.println(data);
        }

    }

}

package com.gigaspaces.rest.resources;

import com.gigaspaces.rest.resources.simple.SimpleDeploymentResourceManager;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by niv on 12/29/2016.
 */
public class ResourcesDemo {
    public static void main(String[] args) throws IOException {
        DeploymentResourceManager manager = new SimpleDeploymentResourceManager(new File("c:\\temp"));

        printResources(manager.getResourcesNames());
        manager.add("foo", new File("c:\\temp\\xap-datagrid.jar"));
        printResources(manager.getResourcesNames());
        manager.add("bar", manager.get("foo"));
        printResources(manager.getResourcesNames());
        manager.remove("foo");
        printResources(manager.getResourcesNames());
        manager.remove("bar");
        printResources(manager.getResourcesNames());
    }

    private static void printResources(Iterator<String> iterator) {
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            if (sb.length() != 0)
                sb.append(",");
            sb.append(iterator.next());
        }
        System.out.println("Resources: [" + sb + "]");
    }
}

package com.gigaspaces.rest.resources.simple;

import com.gigaspaces.rest.resources.DeploymentResourceManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class SimpleDeploymentResourceManager extends DeploymentResourceManager {
    private final File location;

    public SimpleDeploymentResourceManager(File location) {
        this.location = location;
    }

    @Override
    public void add(String name, File resource) throws IOException {
        File resourceLocation = new File(location, name);
        if (resourceLocation.exists())
            throw new IOException("Resource already exists - " + name);
        resourceLocation.mkdir();
        if (!resourceLocation.exists())
            throw new IOException("Resource could not be created - " + name);
        FileUtils.copyFile(resource, new File(resourceLocation, resource.getName()));
    }

    @Override
    public void remove(String name) throws IOException {
        FileUtils.forceDelete(new File(location, name));
    }

    @Override
    public File get(String name) throws IOException {
        File resourceLocation = new File(location, name);
        if (!resourceLocation.exists())
            return null;
        if (!resourceLocation.isDirectory())
            throw new IOException("Resource location is not a directory - " + resourceLocation.getPath());
        final File[] files = resourceLocation.listFiles();
        if (files.length == 0)
            throw new IOException("No files in resource location " + name);
        if (files.length > 1)
            throw new IOException("More than one file in resource location " + name);
        return files[0];
    }

    @Override
    public Iterator<String> getResourcesNames() {
        Collection<String> result = new ArrayList<String>();
        for (File file : location.listFiles()) {
            if (file.isDirectory())
                result.add(file.getName());
        }
        return result.iterator();
    }
}

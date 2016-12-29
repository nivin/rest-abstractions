package com.gigaspaces.rest.resources;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * Abstraction for managing deployment resources.
 * A resource can be a single file (jar, war, zip) or a single directory containing other files and directories.
 * Each resource is mapped to a unique name.
 *
 * GET /deployments/resources           will use getResourcesNames()
 * POST /deployments/resources          will use add(name, resource)
 * DELETE /deployments/resources/{id}   will use remove(name)
 * POST /deployments (deploy pu)        will use get(name) to get the pu's resource
 */
public abstract class DeploymentResourceManager {

    /**
     * Adds the specified resource using the specified name.
     * If there's already another resource by that name, an IOException is thrown.
     * @param name Name of resource to add
     * @param resource Resource file
     */
    public abstract void add(String name, File resource) throws IOException;

    /**
     * Removes the specified resource.
     * @param name Name of resource to remove
     */
    public abstract void remove(String name) throws IOException;

    /**
     * Retrieves a resource by its name. If there's no such resource null is returned
     * @param name Name of resource to fetch
     * @return Matching resource (or null if no such resource)
     */
    public abstract File get(String name) throws IOException;

    /**
     * Gets all resources names
     * @return Resources names
     */
    public abstract Iterator<String> getResourcesNames();
}

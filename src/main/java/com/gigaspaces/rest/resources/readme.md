# Deployment Resources Management Abstraction

## Overview

Deploying a PU requires uploading a resource (jar, war, zip...) to the server. Due to certain limitations in swagger, the deploy pu operation cannot include the file to upload, and we need a separate operation for that (upload/create resource).
We've decided to create an abstraction for that - `DeploymentResourceManager`, and for starters a simple file-based implementation which stores resources file in the local file system.

## Operations mapping

### Deploy PU

Before you deploy the PU, you upload the resource using `POST /deployments/resources`. This will use `DeploymentResourceManager.add(name, resource)`. Then, you create a deployment descriptor with the resource name and call `POST /deployments` to deploy the PU. The implementation will call `DeploymentResourceManager.get(name)` to retrieve the resource and deploy using the Admin API.

### Undeploy PU

Once the PU has been undeployed, you can remove the resource using `DELETE /deployments/resources/{id}`, which will trigger `DeploymentResourceManager.remove(name)`

### List resources

For visibility and debugging, the API allows you to list the uploaded resources using `GET /deployments/resources`, which will be implemented using `DeploymentResourceManager.getResourcesNames()`

package com.ttn.slingexporter.service;

import org.apache.sling.api.resource.ResourceResolver;

public interface HeadlessResourceResolverService {

    ResourceResolver getResourceResolver();
}
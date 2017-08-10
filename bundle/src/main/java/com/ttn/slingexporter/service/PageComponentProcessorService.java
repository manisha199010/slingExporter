package com.ttn.slingexporter.service;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONArray;

import java.util.List;

public interface PageComponentProcessorService {

    JSONArray processComponents(List<Resource> resourcesList);
}

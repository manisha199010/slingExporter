package com.ttn.slingexporter.service;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.commons.json.io.JSONWriter;

import java.util.Iterator;
import java.util.List;

public interface HeadlessComponentJsonWriterService {

    //void write(List<Resource> resourceList) throws Exception;

    void writeKey(JSONWriter out, String key, Object value) throws JSONException;

    JSONObject fetchPropertiesJson(Resource resource);

    JSONArray fetchPropertiesJsonAll(List<Resource> resourceList);

    JSONArray fetchPropertiesJsonAll(Iterator<Resource> resourceItr);
}

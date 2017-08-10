package com.ttn.slingexporter.service;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

public interface PageDataComposeService {

    JSONObject composePageData(ResourceResolver resourceResolver, String resourcePath) throws JSONException;
}

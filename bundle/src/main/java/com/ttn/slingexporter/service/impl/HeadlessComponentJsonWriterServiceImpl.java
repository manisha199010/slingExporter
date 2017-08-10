package com.ttn.slingexporter.service.impl;

import com.ttn.slingexporter.service.ComponentPropertiesService;
import com.ttn.slingexporter.service.HeadlessComponentJsonWriterService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.commons.json.io.JSONWriter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service(HeadlessComponentJsonWriterService.class)
@Component(immediate = true)
public class HeadlessComponentJsonWriterServiceImpl implements HeadlessComponentJsonWriterService {

    @Reference
    ComponentPropertiesService componentPropertiesService;

    private JSONObject write(Resource componentResource){
        JSONObject json = new JSONObject();
        try {
            if (componentResource != null) {
                ValueMap valueMap = componentResource.adaptTo(ValueMap.class);
                List<String> componentProperties = componentPropertiesService.getPropertiesForComponent(componentResource.getResourceType());
                if(componentProperties != null) {
                    Iterator<String> propertiesIterator = componentProperties.iterator();
                    while (propertiesIterator.hasNext()) {
                        String property = propertiesIterator.next();
                        Object value = valueMap.get(property);
                        if (value != null) {
                            if (value.getClass().isArray()) {
                                List list = new ArrayList();
                                for (Object e : (Object[]) value) {
                                    list.add(e);
                                }
                                json.put(property, list);
                            } else {
                                json.put(property, value);
                            }
                        }
                    }
                    json.put("sling:resourceType",valueMap.get("sling:resourceType"));
                }

            }
        }catch (JSONException je){
            je.printStackTrace();
        }

        return json;
    }

    public void writeKey(JSONWriter out, String key, Object value) throws JSONException {
        out.key(key).value(value);
    }

    public JSONObject fetchPropertiesJson(Resource resource) {
            return write(resource);
    }

    public JSONArray fetchPropertiesJsonAll(List<Resource> resourceList) {
        JSONArray jsonArray = new JSONArray();
       if(resourceList != null){
           Iterator<Resource> resourceIterator = resourceList.iterator();
           while(resourceIterator.hasNext()){
           jsonArray.put(write(resourceIterator.next()));
           }
       }
        return jsonArray;
    }

    public JSONArray fetchPropertiesJsonAll(Iterator<Resource> resourceItr) {
        JSONArray jsonArray = new JSONArray();
        if(resourceItr != null){
            while(resourceItr.hasNext()){
                jsonArray.put(write(resourceItr.next()));
            }
        }
        return jsonArray;
    }

}

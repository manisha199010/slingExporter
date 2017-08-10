package com.ttn.slingexporter.service.impl;

import com.ttn.slingexporter.service.HeadlessComponentJsonWriterService;
import com.ttn.slingexporter.service.PageComponentProcessorService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONArray;

import java.util.List;

@Service(PageComponentProcessorService.class)
@Component(metatype = false,immediate = true)
public class PageComponentProcessorServiceImpl implements PageComponentProcessorService {

    @Reference
    HeadlessComponentJsonWriterService headlessComponentJsonWriterService;

    public JSONArray processComponents(List<Resource> resourcesList){
        if(resourcesList != null) {
            return headlessComponentJsonWriterService.fetchPropertiesJsonAll(resourcesList);
        }else{
            return null;
        }
    }
}

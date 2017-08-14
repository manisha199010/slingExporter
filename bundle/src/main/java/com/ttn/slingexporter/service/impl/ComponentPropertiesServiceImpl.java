package com.ttn.slingexporter.service.impl;


import com.ttn.slingexporter.service.ComponentPropertiesService;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;

import java.util.*;

@Service(ComponentPropertiesService.class)
@Component(metatype = true, label = "Headless Component properties Service", description = "Add resource type and their properties that needs to be exposed ", immediate = true)
@Property(name = "component.path", label = "Component paths", description = "The component path for translation", value = {
        "foundation/components/text=text;textIsRich",
        "foundation/components/textimage=text",
        "foundation/components/image=fileReference;alt;jcr:title;jcr:description;width;height",
        "invest-india/components/content/image=fileReference;alt;jcr:title;jcr:description;width;height",
        "foundation/components/video=asset;width;height",
        "foundation/components/title=jcr:title;size",
        "foundation/components/search=jcr:title;searchIn",
        "foundation/components/table=tableData",
        "foundation/components/download=jcr:title;fileReference",
        "foundation/components/chart=chartType;chartData",
        "foundation/components/carousel=pages;playSpeed;transTime",
        "geometrixx/components/title=jcr:title",
        "geometrixx/components/header=jcr:title;text",
        "invest-india/components/content/bloglist=authorName;blogTitle;blogdescription;publishDate",
        "invest-india/components/content/blogcomponent=authorName;blogTitle;blogdescription;publishDate;path",
        "invest-india/components/content/text=text",
        "foundation/components/table=tableData"
}, cardinality = Integer.MAX_VALUE)
public class ComponentPropertiesServiceImpl implements ComponentPropertiesService {

    Map<String, List<String>> validPropertiesMap = new HashMap<String, List<String>>();


    @Activate
    protected void activate(ComponentContext componentContext) throws Exception {
        Dictionary props = componentContext.getProperties();
        String[] list = (String[]) props.get("component.path");
        for (String config : list) {
            String key = (config).split("=")[0];
            String value = (config).split("=")[1];
            List<String> valueList = Arrays.asList(value.split(";"));
            validPropertiesMap.put(key, valueList);
        }
    }

    public List<String> getPropertiesForComponent(String component) {
        return validPropertiesMap.get(component);
    }

    public boolean containsComponent(String component) {
        return validPropertiesMap.containsKey(component);
    }

}

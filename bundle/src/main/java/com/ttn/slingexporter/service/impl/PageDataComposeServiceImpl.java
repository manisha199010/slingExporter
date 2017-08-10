package com.ttn.slingexporter.service.impl;

import com.day.cq.commons.jcr.JcrConstants;
import com.ttn.slingexporter.service.ComponentPropertiesService;
import com.ttn.slingexporter.service.PageComponentProcessorService;
import com.ttn.slingexporter.service.PageDataComposeService;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.osgi.service.component.ComponentContext;

import java.util.*;

@Component(immediate = true)
@Service(PageDataComposeService.class)
@Property(name = "template.path", label = "Templates paths", description = "The template path for translation", value = {
        "invest-india/components/page/one-column-page=col-1",
        "invest-india/components/page/two-column-page=col-1;col-2",
        "foundation/components/table=tableData"
}, cardinality = Integer.MAX_VALUE)

public class PageDataComposeServiceImpl implements PageDataComposeService {

 /*   @Reference
    private HeadlessResourceResolverService resourceResolverService;*/

    Map<String, List<String>> validPropertiesMap = new HashMap<String, List<String>>();

    public static final String BLOG_TEMPLATE = "";

    @Activate
    protected void activate(ComponentContext componentContext) throws Exception {
        Dictionary props = componentContext.getProperties();
        String[] list = (String[]) props.get("template.path");
        for (String config : list) {
            String key = (config).split("=")[0];
            String value = (config).split("=")[1];
            List<String> valueList = Arrays.asList(value.split(";"));
            validPropertiesMap.put(key, valueList);
        }
    }

    @Reference
    private PageComponentProcessorService pageComponentProcessorService;

    @Reference
    ComponentPropertiesService componentPropertiesService;



   /* public ResourceResolver getResourceResolver(){
        return this.resourceResolverService.getResourceResolver();
    }*/

    JSONArray jsonArray = new JSONArray();

    JSONObject page = new JSONObject();

    public JSONObject composePageData(ResourceResolver resourceResolver, String resourcePath) throws JSONException {
        Resource resource = resourceResolver.resolve(resourcePath);
        JSONArray contentList = new JSONArray();

        if (resource != null && !Resource.RESOURCE_TYPE_NON_EXISTING.equals(resource.getResourceType())) {
            ValueMap valueMap = resource.adaptTo(ValueMap.class);
            page.put("title", valueMap.get("jcr:title"));
            page.put("description", valueMap.get("jcr:description"));

            String pageResourceType = valueMap.get("sling:resourceType", String.class);
            if (validPropertiesMap.containsKey(pageResourceType)) {
                List<String> parsysNodes = validPropertiesMap.get(pageResourceType);
                for (String parsysNode : parsysNodes) {     // for col nodes
                    Resource colChild = resource.getChild(parsysNode);
                    if (colChild != null) {
                        Iterator<Resource> children = colChild.listChildren();

                        while (children.hasNext()) {            // list of resources in col parsys
                            contentList = composeComponentData(children.next(), resource, new JSONObject());
                        }
                    }

                }
                page.put("content", contentList);
            }
        }
        return page;
    }

    private JSONArray composeComponentData(Resource child, Resource resource, JSONObject jsonObject) throws JSONException {
        JSONArray contentList = new JSONArray();
        switch (child.getValueMap().get("sling:resourceType", String.class)) {
            case ComponentPropertiesService.BLOG_LIST_COMPONENT:
                Iterator<Resource> blogResourceItr = resource.getParent().listChildren();
                while (blogResourceItr.hasNext()) {
                    Resource blogResource = blogResourceItr.next();
                    JSONObject content = new JSONObject();
                    if (blogResource.getValueMap().get(JcrConstants.JCR_PRIMARYTYPE, String.class).equals("cq:Page")) {
                        Resource blogCompRes = blogResource.getChild("jcr:content/blogcomponent");
                        if (blogCompRes != null) {
                            content = addComponents(blogCompRes, content);
                        }
                    }
                    if (content.length() > 0) {
                        contentList.put(content);
                    }
                }
                break;
            default:
                JSONObject content = addComponents(child, new JSONObject());
                if (content.length() > 0) {
                    contentList.put(content);
                }

        }
        return contentList;
    }

    private JSONObject addComponents(Resource r, JSONObject content) throws JSONException {
        JSONObject node = new JSONObject();
        if (componentPropertiesService.containsComponent(r.getResourceType())) {
            List<String> properties = componentPropertiesService.getPropertiesForComponent(r.getResourceType());
            ValueMap propertiesMap = r.adaptTo(ValueMap.class);
            for (String p : properties) {

                if (propertiesMap.containsKey(p)) {
                    String val = propertiesMap.get(p).toString();
                    processData(val, p, node);
                } else if (p.equals("path")) {
                    processData(r.getPath() + "jcr:content.compose.json", p, node);
                }

            }
            //content.put(r.getName(), node);
        }
        return node;
    }

    private void processData(String data, String type, JSONObject node) throws JSONException {
        if (type.equalsIgnoreCase("text")) {
            Document doc = Jsoup.parse(data);
            Elements links = doc.getElementsByTag("a");
            data = data.replaceAll("\\<.*?>", "");
            node.put(type, data);
            JSONArray linkArr = new JSONArray();
            for (Element link : links) {
                JSONObject linkObj = new JSONObject();
                String href = link.attr("href");
                String text = link.text();
                linkObj.put("text", text);
                linkObj.put("href", href);
                linkArr.put(linkObj);
            }
            if (linkArr.length() > 0) {
                node.put("a", linkArr);
            }

        } else if (type.equalsIgnoreCase("tableData")) {
            Document doc = Jsoup.parse(data);
            Elements rows = doc.getElementsByTag("tr");
            JSONArray rowArray = new JSONArray();
            for (Element row : rows) {
                Elements cols = row.getElementsByTag("td");
                JSONArray colArray = new JSONArray();
                for (Element col : cols) {
                    JSONObject column = new JSONObject();
                    Elements links = col.getElementsByTag("a");
                    JSONArray linkArr = new JSONArray();
                    for (Element link : links) {
                        JSONObject linkObj = new JSONObject();
                        String href = link.attr("href");
                        String text = link.text();
                        linkObj.put("text", text);
                        linkObj.put("href", href);
                        linkArr.put(linkObj);
                    }
                    column.put("text", col.text());
                    if (linkArr.length() > 0) {
                        column.put("a", linkArr);
                    }

                    colArray.put(column);
                }
                rowArray.put(colArray);
            }
            node.put(type, rowArray);
        } else if(type.equalsIgnoreCase("publishDate")){
            node.put(type, new Date());
        } else{
            data = data.replaceAll("\\<.*?>", "");
            node.put(type, data);
        }
        node.put("type", type);
    }
}

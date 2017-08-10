package com.ttn.slingexporter.servlets;

import com.ttn.slingexporter.service.PageDataComposeService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import java.io.IOException;

@Component(immediate = true)
@Service
@Properties({    @Property(name = "sling.servlet.resourceTypes",
        value = {"sling/servlet/default"}
),     @Property(
        name = "sling.servlet.selectors",
        value = {"compose"}
),     @Property(
        name = "sling.servlet.extensions",
        value = {"json"}
),     @Property(
        name = "sling.servlet.methods",
        value = {"GET"}
)})
public class PageDataComposerServlet extends SlingAllMethodsServlet {

    @Reference
    PageDataComposeService pageDataComposeService;

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try {
            JSONObject jsonObject = pageDataComposeService.composePageData(request.getResourceResolver(),request.getResource().getPath());
            jsonObject.write(response.getWriter());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

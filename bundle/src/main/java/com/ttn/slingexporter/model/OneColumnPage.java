package com.ttn.slingexporter.model;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Created by ttnd on 19/7/17.
 */
@Model(adaptables = Resource.class, resourceType = { "invest-india/components/page/one-column-page" }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json", options = { @ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "true") })
public class OneColumnPage {

    @Inject @Named("jcr:title")
    String title;

   /* @ChildResource @Named("col-1")
    private ParsysModel data;*/

    @ChildResource @Named("col-1")
    private List<InvestIndiaTextComponent> data;

   /* public ParsysModel getData() {
        return data;
    }

    public void setData(ParsysModel data) {
        this.data = data;
    }
*/

    public List<InvestIndiaTextComponent> getData() {
        return data;
    }

    public void setData(List<InvestIndiaTextComponent> data) {
        this.data = data;
    }
}

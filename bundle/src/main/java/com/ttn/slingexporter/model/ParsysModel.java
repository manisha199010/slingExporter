package com.ttn.slingexporter.model;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ttnd on 19/7/17.
 */
@Model(adaptables = Resource.class, resourceType = {"wcm/foundation/components/parsys"},defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json", options = { @ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "true") })
public class ParsysModel {

    /*@Inject @Named("sling:resourceType")
    String resourceType;*/

   /* @ChildResource @Named("text")
    InvestIndiaTextComponent text;
*/
    /*@ChildResource @Named("text1")
    InvestIndiaTextComponent text1;

    @ChildResource @Named("table")
    InvestIndiaTextComponent table;*/


    private Resource resource;

    @Inject @Named(".")
    public Iterator<Resource> getChildren() {
        return resource.listChildren();
    }

   /* public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
*/
   /* public InvestIndiaTextComponent getText() {
        return text;
    }

    public void setText(InvestIndiaTextComponent text) {
        this.text = text;
    }
*/
   /* public InvestIndiaTextComponent getTable() {
        return table;
    }

    public void setTable(InvestIndiaTextComponent table) {
        this.table = table;
    }*/
/*  @PostConstruct
  private void init(){
      for (InvestIndiaTextComponent comp: getComp()) {
          System.out.println(comp.getText());
      }
  }*/
    /*public List<InvestIndiaTextComponent> getComp() {
        return comp;
    }

    public void setComp(List<InvestIndiaTextComponent> comp) {
        this.comp = comp;
    }*/

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}

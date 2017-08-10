package com.ttn.slingexporter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by ttnd on 16/7/17.
 */
@Model(adaptables = Resource.class, resourceType = { "invest-india/components/content/text","foundation/components/table" }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json", options = { @ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "true") })
public class InvestIndiaTextComponent{

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Inject
    private String text;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Inject
    private String tableData;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTableData() {
        return tableData;
    }

    public void setTableData(String tableData) {
        this.tableData = tableData;
    }

    @PostConstruct
    private void init() {
        processTextData();
        processTableData();
    }

    private void processTextData(){
        if(this.text != null) {
            this.text = this.text.replaceAll("\\<.*?>", "");
        }
    }

    private void processTableData(){
        if(this.tableData != null) {
            this.tableData = this.tableData.replaceAll("\\<.*?>", "");
        }
    }
}

package io.praegus.bda.locationservice.adapter.apipostcode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OverPassElement {
    public long id;
    public String type;
    public double lat;
    public double lon;
    public Map<String, String> tags;
}
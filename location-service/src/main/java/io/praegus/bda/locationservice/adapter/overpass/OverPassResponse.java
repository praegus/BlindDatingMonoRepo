package io.praegus.bda.locationservice.adapter.overpass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OverPassResponse {
    public List<OverPassElement> elements;
}
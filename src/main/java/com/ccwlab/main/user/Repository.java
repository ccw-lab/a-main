package com.ccwlab.main.user;

import com.ccwlab.main.work.Work;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Repository {
    String name;
    String description;
    String visibility;
    @JsonProperty("html_url")
    String htmlUrl;
    String id;
    boolean enabled;
    String url;
    @JsonProperty("active_work_list")
    List<Work> activeWorkList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

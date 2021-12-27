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
    long id;
    @JsonProperty("onwer_id")
    long ownerId;
    boolean enabled;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Repository(String name, String description, String visibility, String htmlUrl, long id, long ownerId, boolean enabled) {
        this.name = name;
        this.description = description;
        this.visibility = visibility;
        this.htmlUrl = htmlUrl;
        this.id = id;
        this.ownerId = ownerId;
        this.enabled = enabled;
    }
}

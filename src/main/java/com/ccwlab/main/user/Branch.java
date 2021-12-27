package com.ccwlab.main.user;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Branch{
    String name;
    String sha;
    @JsonProperty("ci_file_exist")
    boolean ciFileExist;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public boolean isCiFileExist() {
        return ciFileExist;
    }

    public void setCiFileExist(boolean ciFileExist) {
        this.ciFileExist = ciFileExist;
    }

    public Branch(String name, String sha, boolean ciFileExist) {
        this.name = name;
        this.sha = sha;
        this.ciFileExist = ciFileExist;
    }
}
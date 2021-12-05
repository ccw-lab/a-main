package com.ccwlab.main.work;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Optional;

public class Work {
    int id;
    @JsonProperty("repository_id")
    String repositoryId;
    @JsonProperty("commit_id")
    String commitId;
    @JsonProperty("commit_message")
    String commitMessage;
    @JsonProperty("started_time")
    Optional<Instant> startedTime;
    @JsonProperty("completed_time")
    Optional<Instant> completedTime;
    @JsonProperty("failed_time")
    Optional<Instant> failedTime;
    WorkStatus status;
    @JsonProperty("run_script")
    String runScript;
    String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public Optional<Instant> getStartedTime() {
        return startedTime;
    }

    public void setStartedTime(Optional<Instant> startedTime) {
        this.startedTime = startedTime;
    }

    public Optional<Instant> getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(Optional<Instant> completedTime) {
        this.completedTime = completedTime;
    }

    public Optional<Instant> getFailedTime() {
        return failedTime;
    }

    public void setFailedTime(Optional<Instant> failedTime) {
        this.failedTime = failedTime;
    }

    public WorkStatus getStatus() {
        return status;
    }

    public void setStatus(WorkStatus status) {
        this.status = status;
    }

    public String getRunScript() {
        return runScript;
    }

    public void setRunScript(String runScript) {
        this.runScript = runScript;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
enum WorkStatus{
    STARTED,
    STOPPED,
    FAILED,
    COMPLETED
}

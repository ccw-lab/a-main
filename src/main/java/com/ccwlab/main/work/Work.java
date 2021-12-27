package com.ccwlab.main.work;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;
import java.time.Instant;
import java.util.Optional;

public class Work {
    @JsonProperty("repository_id")
    long repositoryId;
    @JsonProperty("repository_name")
    String repositoryName;
    @JsonProperty("commit_id")
    String commitId;
    @JsonProperty("commit_message")
    String commitMessage;
    @JsonProperty("started_time")
    Instant startedTime;
    @JsonProperty("completed_time")
    Instant completedTime;
    @JsonProperty("stopped_time")
    Instant stoppedTime;
    @JsonProperty("failed_time")
    Instant failedTime;
    WorkStatus status;
    @JsonProperty("work_id")
    long workId;
    @JsonProperty("work_progress_uri")
    String workProgressURI;

    public String getWorkProgressURI() {
        return workProgressURI;
    }

    public void setWorkProgressURI(String workProgressURI) {
        this.workProgressURI = workProgressURI;
    }

    public Work() {
    }

    public Work(long repositoryId, String repositoryName, String commitId, String commitMessage, Instant startedTime, Instant completedTime, Instant stoppedTime, Instant failedTime, WorkStatus status, long workId, String workProgressURI) {
        this.repositoryId = repositoryId;
        this.repositoryName = repositoryName;
        this.commitId = commitId;
        this.commitMessage = commitMessage;
        this.startedTime = startedTime;
        this.completedTime = completedTime;
        this.stoppedTime = stoppedTime;
        this.failedTime = failedTime;
        this.status = status;
        this.workId = workId;
        this.workProgressURI = workProgressURI;
    }

    @Override
    public String toString() {
        return "Work{" +
                "repositoryId=" + repositoryId +
                ", repositoryName='" + repositoryName + '\'' +
                ", commitId='" + commitId + '\'' +
                ", commitMessage='" + commitMessage + '\'' +
                ", startedTime=" + startedTime +
                ", completedTime=" + completedTime +
                ", stoppedTime=" + stoppedTime +
                ", failedTime=" + failedTime +
                ", status=" + status +
                ", workId=" + workId +
                ", workProgressURI='" + workProgressURI + '\'' +
                '}';
    }

    public Instant getStoppedTime() {
        return stoppedTime;
    }

    public void setStoppedTime(Instant stoppedTime) {
        this.stoppedTime = stoppedTime;
    }

    public long getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(long repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
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

    public Instant getStartedTime() {
        return startedTime;
    }

    public void setStartedTime(Instant startedTime) {
        this.startedTime = startedTime;
    }

    public Instant getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(Instant completedTime) {
        this.completedTime = completedTime;
    }

    public Instant getFailedTime() {
        return failedTime;
    }

    public void setFailedTime(Instant failedTime) {
        this.failedTime = failedTime;
    }

    public WorkStatus getStatus() {
        return status;
    }

    public void setStatus(WorkStatus status) {
        this.status = status;
    }

    public long getWorkId() {
        return workId;
    }

    public void setWorkId(long workId) {
        this.workId = workId;
    }
}
enum WorkStatus{
    STARTED,
    FAILED,
    COMPLETED
}

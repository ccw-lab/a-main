package com.ccwlab.main.work;

import com.ccwlab.main.message.FinalReport;
import com.ccwlab.main.message.MyProcessor;
import com.ccwlab.main.message.ProgressReport;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("works")
public class WorkController {
    Logger log = LoggerFactory.getLogger(WorkController.class);

    @Autowired
    private MyProcessor processor;

    @GetMapping("/test")
    void test(){
        var req = new WorkRequest();
        req.accessToken="1";
        req.commitId="1";
        req.repositoryId="2";
        req.repositoryName="repository_name";
        req.requestedTime = Instant.now();
        log.debug("send: " + req.toString());
        this.processor.output().send(MessageBuilder.withPayload(req).build());
    }

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/test2")
    String test2(){
        var html = this.restTemplate.getForObject("http://web:4200/", String.class);
        log.debug("http://web/" + html);
        return html;
    }

    @GetMapping("/test3")
    String test3(){
        log.debug("http://controller/" + this.restTemplate.getForObject("http://controller/actuator/health", String.class));
        return this.restTemplate.getForObject("http://controller/actuator/health", String.class);
    }

    class WorkRequest implements Serializable {
        @JsonProperty("requested_time")
        Instant requestedTime;
        String repositoryName;
        String repositoryId;
        String commitId;
        String accessToken;

        @Override
        public String toString() {
            return "WorkRequest{" +
                    "requestedTime=" + requestedTime +
                    ", repositoryName='" + repositoryName + '\'' +
                    ", repositoryId='" + repositoryId + '\'' +
                    ", commitId='" + commitId + '\'' +
                    ", accessToken='" + accessToken + '\'' +
                    '}';
        }

        public Instant getRequestedTime() {
            return requestedTime;
        }

        public void setRequestedTime(Instant requestedTime) {
            this.requestedTime = requestedTime;
        }

        public String getRepositoryName() {
            return repositoryName;
        }

        public void setRepositoryName(String repositoryName) {
            this.repositoryName = repositoryName;
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

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
    }

    @PostMapping()
    @Operation(description = "Start new CI/CD work for a specific commit.", responses = {
            @ApiResponse(responseCode = "200", description = "New work is created and started successfully."),
            @ApiResponse(responseCode = "500", description = "A work creation failed.")
    })
    ResponseEntity<Work> createWork(@Parameter(description = "Object containing a specific commit") @RequestBody  CreateWork createWork){
        throw new UnsupportedOperationException();
    }

    @PutMapping("/{workId}")
    @Operation(description = "Stop a specific work by passing status property.")
    @ApiResponse(responseCode = "200", description = "A status has been updated successfully.")
    ResponseEntity<Work> updateWork(@Parameter(description = "A work id") @PathVariable String workId,
                                    @Parameter(description = "Object containing status you expect as new status") @RequestBody UpdateWork updateWork){
        throw new UnsupportedOperationException();
    }

    @GetMapping("/{workId}")
    @Operation(description = "Get information of a work.")
    @ApiResponse(responseCode = "200", description = "A requested work object")
    @ApiResponse(responseCode = "404", description = "A requested work object was not found.")
    ResponseEntity<Work> getWork(@Parameter(description = "A work id") @PathVariable String workId){
        throw new UnsupportedOperationException();
    }

    @GetMapping
    @Operation(description = "Get a list of previous works following search conditions.")
    @ApiResponse(responseCode = "200", description = "A list of works")
    ResponseEntity<List<Work>> getWorkList(@Parameter(description = "from time") @RequestParam(value = "from", required = false) Instant from,
                                           @Parameter(description = "to time") @RequestParam("to") Instant to){
        throw new UnsupportedOperationException();
    }

    @StreamListener(MyProcessor.in)
    void onFinalReport(FinalReport report) {
        throw new UnsupportedOperationException();
    }

    @StreamListener(MyProcessor.in)
    void onProgressReport(ProgressReport report) {
        throw new UnsupportedOperationException();
    }
}

class UpdateWork {
    WorkStatus status;

    public WorkStatus getStatus() {
        return status;
    }
    public void setStatus(WorkStatus status) {
        this.status = status;
    }
}

class CreateWork {
    @JsonProperty("repository_id")
    String repositoryId;
    @JsonProperty("commit_id")
    String commitId;

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
}

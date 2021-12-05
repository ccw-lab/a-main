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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("works")
public class WorkController {

    @Autowired
    private MyProcessor processor;

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

    @StreamListener(MyProcessor.channel)
    void onFinalReport(FinalReport report) {
        throw new UnsupportedOperationException();
    }

    @StreamListener(MyProcessor.channel)
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

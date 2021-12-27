package com.ccwlab.main.work;

import com.ccwlab.main.JwtUtil;
import com.ccwlab.main.GithubUtil;
import com.ccwlab.main.mapper.WorkMapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;

@RestController
@RequestMapping("works")
public class WorkController {
    Logger log = LoggerFactory.getLogger(WorkController.class);

    @PostConstruct
    void setup(){
        this.workMapper.createTable();
    }

    @Autowired
    private StreamBridge streamBridge;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    GithubUtil githubUtil;

    @Autowired
    WorkMapper workMapper;

    @Autowired
    DiscoveryClient discoveryClient;

    Logger logger = LoggerFactory.getLogger(WorkController.class);

    @Autowired
    private CircuitBreakerFactory cbFactory;

    @PostMapping()
    @Operation(description = "Start new CI/CD work for a specific commit.", responses = {
            @ApiResponse(responseCode = "200", description = "A new work is created and started successfully."),
            @ApiResponse(responseCode = "500", description = "A work creation failed.")
    })
    ResponseEntity<Work> requestWork(@Parameter(description = "A CI/CD work will be running on this specific commit")
                                     @RequestBody CreateWork createWork
            , @RequestHeader("Authorization") String header) {
        var accessToken = jwtUtil.getDecodedJwt(header).getClaim("t").asString();
        var github = this.githubUtil.get(accessToken);
        var existingWorks = workMapper.selectByRepoNameAndCommitId(createWork.repositoryName, createWork.commitId);
        if(existingWorks.size() > 0){
            return ResponseEntity.ok(existingWorks.get(0));
        }
        var requestRepoName = createWork.getRepositoryName();
        var requestCommitId = createWork.getCommitId();
        try {
            var myself = github.getMyself();
            var repo = myself.getRepository(requestRepoName);
            var commit = repo.getCommit(requestCommitId);
            var req = new WorkRequest(Instant.now(), repo.getName(), repo.getId(), commit.getSHA1(), accessToken);
            logger.debug("req: " + req.toString());

            var entity = cbFactory.create("request to controller").run(() ->
                            this.restTemplate.postForEntity("http://controller/works", req, Long.class)
                    , ex -> null);
            if(entity == null){
                logger.debug("Controller service is too slow now. Return 503;");
                return ResponseEntity.status(503).build();
            }
            if(entity.getStatusCode() == HttpStatus.ACCEPTED) {
                var workId = entity.getBody();
                var work = new Work(repo.getId(), repo.getName(), requestCommitId, commit.getCommitShortInfo().getMessage(), Instant.now(), null, null, null, WorkStatus.STARTED, workId
                        , discoveryClient.getInstances("gateway").get(0).getUri().toString() + "/works/" + workId + "/logs");
                logger.debug("work: " + work.toString());
                workMapper.insert(work);
                return ResponseEntity.ok(work);
            }else
                return ResponseEntity.internalServerError().build();
        }catch(IOException ex){
            logger.debug("ex", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

//    @PutMapping("/{workId}")
//    @Operation(description = "Stop a specific work by passing status property.")
//    @ApiResponse(responseCode = "200", description = "A status has been updated successfully.")
//    ResponseEntity<Work> updateWork(@Parameter(description = "A work id") @PathVariable long workId,
//                                    @Parameter(description = "Object containing status you expect as new status") @RequestBody UpdateWork updateWork
//            , @RequestHeader("Authorization") String header) {
//        var accessToken = jwtUtil.getDecodedJwt(header).getClaim("t").asString();
//        var github = this.githubUtil.get(accessToken);
//        var workList = this.workMapper.selectByWorkId(workId);
//        if(workList.size() == 1){
//            var work = workList.get(0);
//            try {
//                var myself = github.getMyself();
//                var foundInGithub = github.getRepositoryById(work.getRepositoryId());
//                if (foundInGithub.getOwner().getId() == myself.getId()) {
//                    var entity = this.restTemplate.exchange("http://controller/works/" + workId
//                            , HttpMethod.PUT
//                            , new HttpEntity<>(new StopWork(PutOperation.STOP, accessToken))
//                            , Long.class);
//                    if(entity.getStatusCode() == HttpStatus.OK) {
//                        work.setStatus(WorkStatus.STOPPED);
//                        work.setStoppedTime(Instant.now());
//                        this.workMapper.insert(work);
//                        return ResponseEntity.ok(work);
//                    }
//                }
//            }catch(IOException e){
//                return ResponseEntity.badRequest().build();
//            }
//        }
//        return ResponseEntity.badRequest().build();
//    }

    @GetMapping("/{workId}")
    @Operation(description = "Get information of a work.")
    @ApiResponse(responseCode = "200", description = "A requested work object")
    @ApiResponse(responseCode = "404", description = "A requested work object was not found.")
    ResponseEntity<Work> getWork(@Parameter(description = "A work id") @PathVariable long workId){
        var workList = workMapper.selectByWorkId(workId);
        if(workList.size() == 1)
            return ResponseEntity.ok(workList.get(0));
        else
            return ResponseEntity.internalServerError().build();
    }

    @GetMapping
    @Operation(description = "Get a list of works containing a provided repository id.")
    @ApiResponse(responseCode = "200", description = "A list of works")
    ResponseEntity<List<Work>> getWorkList(@Parameter(description = "A repository id")
                                           @RequestParam(value = "repository_id")
                                           long repositoryId ,
                                           @RequestHeader("Authorization") String header) {
        var accessToken = jwtUtil.getDecodedJwt(header).getClaim("t").asString();
        var github = this.githubUtil.get(accessToken);
        try {
            var myself = github.getMyself();
            if (github.getRepositoryById(repositoryId).getOwner().getId() == myself.getId()) {
                var list = this.workMapper.selectByWorkRepositoryId(repositoryId);
                return ResponseEntity.ok(list);
            }else
                return ResponseEntity.badRequest().build();
        }catch(IOException e){
            logger.debug("ex", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Bean
    Consumer<MainReport> messageFromController(){
        return report -> {
            log.debug("Report!: " + report.toString());
            var workList = this.workMapper.selectByWorkId(report.workId);
            if(workList.size() == 1) {
                var work = workList.get(0);
                if (report.getStatus() == MainReportStatus.COMPLETED) {
                    work.setCompletedTime(report.createdTime);
                    work.setStatus(WorkStatus.COMPLETED);
                    this.workMapper.insert(work);
                } else if (report.getStatus() == MainReportStatus.FAILED) {
                    work.setFailedTime(report.createdTime);
                    work.setStatus(WorkStatus.FAILED);
                    this.workMapper.insert(work);
                }
            }else{
                log.warn("A unknown workId arrived.");
            }
        };
    }
}

class CreateWork {
    @JsonProperty("repository_name")
    String repositoryName;
    @JsonProperty("commit_id")
    String commitId;

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
}

class WorkRequest implements Serializable {
    @JsonProperty("requested_time")
    Instant requestedTime;
    @JsonProperty("repository_name")
    String repositoryName;
    @JsonProperty("repository_id")
    long repositoryId;
    @JsonProperty("commit_id")
    String commitId;
    String accessToken;

    public WorkRequest() {
    }

    public WorkRequest(Instant requestedTime, String repositoryName, long repositoryId, String commitId, String accessToken) {
        this.requestedTime = requestedTime;
        this.repositoryName = repositoryName;
        this.repositoryId = repositoryId;
        this.commitId = commitId;
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "WorkRequest{" +
                "requestedTime=" + requestedTime +
                ", repositoryName='" + repositoryName + '\'' +
                ", repositoryId=" + repositoryId +
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

    public long getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(long repositoryId) {
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

class MainReport {
    long workId;
    MainReportStatus status;
    Instant createdTime;

    @Override
    public String toString() {
        return "MainReport{" +
                "workId=" + workId +
                ", status=" + status +
                ", createdTime=" + createdTime +
                '}';
    }

    public long getWorkId() {
        return workId;
    }

    public void setWorkId(long workId) {
        this.workId = workId;
    }

    public MainReportStatus getStatus() {
        return status;
    }

    public void setStatus(MainReportStatus status) {
        this.status = status;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }
}
enum MainReportStatus{
    STARTED,
    COMPLETED,
    FAILED
}
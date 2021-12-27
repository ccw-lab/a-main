package com.ccwlab.main.user;

import com.ccwlab.main.JwtUtil;
import com.ccwlab.main.GithubUtil;
import com.ccwlab.main.mapper.RepositoryMapper;
import com.ccwlab.main.mapper.WorkMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.kohsuke.github.GHTreeEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    RepositoryMapper repositoryMapper;

    @PostConstruct
    private void setup(){
        this.repositoryMapper.createTable();
    }

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    GithubUtil githubUtil;

    @GetMapping("")
    @Operation(description="Get the information of myself.")
    @ApiResponse(responseCode = "200", description = "Get operation is successful.")
    @ApiResponse(responseCode = "500", description = "A access token or this server is wrong.")
    ResponseEntity<User> getMyself(@RequestHeader("Authorization") String header){
        var accessToken = jwtUtil.getDecodedJwt(header).getClaim("t").asString();
        try {
            var github = this.githubUtil.get(accessToken);
            var myself = github.getMyself();

            return ResponseEntity.ok(new User(myself.getId(), myself.getLogin(), myself.getAvatarUrl(), myself.getHtmlUrl().toString(), myself.getName(), myself.getCompany(), myself.getEmail()));
        }catch(IOException e){
            logger.debug("ex",e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{login}")
    @Operation(description="Get the information of a provided id.")
    @ApiResponse(responseCode = "200", description = "Get operation is successful.")
    @ApiResponse(responseCode = "500", description = "A access token or this server is wrong.")
    ResponseEntity<User> getUser(@Parameter(description = "An login which you want to get information of") @PathVariable String login, @RequestHeader("Authorization") String header){
        var accessToken = jwtUtil.getDecodedJwt(header).getClaim("t").asString();
        var github = this.githubUtil.get(accessToken);
        try {
            var githubUser = github.getUser(login);
            //long id, String login, String avatarUrl, String htmlUrl, String name, String company, String email
            var user = new User(githubUser.getId(), githubUser.getLogin(), githubUser.getAvatarUrl().toString(), githubUser.getHtmlUrl().toString(), githubUser.getName(), githubUser.getCompany(), githubUser.getEmail());
            return ResponseEntity.ok(user);
        }catch(IOException e){
            logger.debug("ex", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{login}/repositories")
    @Operation(description="Get a list of user's repositories.")
    @ApiResponse(responseCode = "200", description = "A requested list")
    @ApiResponse(responseCode = "401", description = "It seems a illegal access.")
    @ApiResponse(responseCode = "500", description = "something is wrong.")
    ResponseEntity<List<Repository>> getRepositories(
            @Parameter(description = "An login which you want to get information of")
            @PathVariable String login, @RequestHeader("Authorization") String header){
        var accessToken = jwtUtil.getDecodedJwt(header).getClaim("t").asString();
        var github = this.githubUtil.get(accessToken);
        try {
            var currentLogin = github.getMyself().getLogin();
            if(github.getMyself().getLogin().equals(login)) {
                var repos = github
                        .getMyself()
                        .getAllRepositories()
                        .entrySet()
                        .stream()
                        .map(e -> e.getValue());
                var enabledList = this.repositoryMapper.select(github.getMyself().getId());
                var result = repos.map(repo -> {
                    var enabled = Arrays.stream(enabledList).filter(item -> item.getId() == repo.getId()).findFirst().map(e -> e.isEnabled()).orElseGet(() -> false);
                    //String name, String description, String visibility, String htmlUrl, int id, int ownerId, boolean enabled, String url, List< Work > activeWorkList
                    long ownerId = 0L;
                    try {
                        ownerId = repo.getOwner().getId();
                    } catch (IOException ex) {
                        logger.debug("ex", ex);
                    }
                    return new Repository(repo.getName(), repo.getDescription(), repo.getVisibility().toString(), repo.getHtmlUrl().toString(), repo.getId(), ownerId, enabled);
                }).toList();
                return ResponseEntity.ok(result);
            }else{
                logger.debug(currentLogin + " and " + login );
                return ResponseEntity.status(401).build();
            }
        }catch(IOException e){
            logger.debug("ex", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @RequestMapping(value = "/{login}/repositories/{repositoryName}", method={RequestMethod.PUT, RequestMethod.POST})
    @Operation(description="Enable or disable CI/CD on this repository.")
    @ApiResponse(responseCode = "200", description = "This repository has been updated successfully.")
    @ApiResponse(responseCode = "401", description = "It seems a illegal access.")
    @ApiResponse(responseCode = "500", description = "something is wrong.")
    ResponseEntity<Repository> getRepositories(@Parameter(description = "object containing whether CI/CD is enabled") @RequestBody UpdateRepository updateRepository,
                                               @Parameter(description = "An login which you update the repository with")
                                                @PathVariable String login,
                                               @Parameter(description = "A repository name which you update the repository with")
                                                @PathVariable String repositoryName
            , @RequestHeader("Authorization") String header) {
        var accessToken = jwtUtil.getDecodedJwt(header).getClaim("t").asString();
        var github = this.githubUtil.get(accessToken);
        try {
            var currentLogin = github.getMyself().getLogin();
            if(github.getMyself().getLogin().equals(login)) {
                var gitRepo = github.getMyself().getRepository(repositoryName);
                var count = repositoryMapper.insert(gitRepo.getId(), github.getMyself().getId(), updateRepository.enabled);
                if(count > 0) {
                    var repo = new Repository(gitRepo.getName(), gitRepo.getDescription(), gitRepo.getVisibility().toString(), gitRepo.getHtmlUrl().toString(), gitRepo.getId(), gitRepo.getOwner().getId(), updateRepository.enabled);
                    return ResponseEntity.ok(repo);
                }else{
                    logger.debug("A merge failed. count: " + count);
                    return ResponseEntity.internalServerError().build();
                }
            }else{
                logger.debug(currentLogin + " and " + login );
                return ResponseEntity.status(401).build();
            }
        }catch(IOException e){
            logger.debug("ex", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{login}/repositories/{repositoryName}/branches")
    @Operation(description="Get a list of branches.")
    @ApiResponse(responseCode = "200", description = "This repository has been updated successfully.")
    @ApiResponse(responseCode = "401", description = "It seems a illegal access.")
    @ApiResponse(responseCode = "500", description = "something is wrong.")
    ResponseEntity<List<Branch>> getBranches(@Parameter(description = "An login which you get branches with")
                                                @PathVariable String login,
                                             @Parameter(description = "A repository name which you get branches with")
                                                @PathVariable String repositoryName
            , @RequestHeader("Authorization") String header) {
        var accessToken = jwtUtil.getDecodedJwt(header).getClaim("t").asString();
        var github = this.githubUtil.get(accessToken);
        try {
            var currentLogin = github.getMyself().getLogin();
            if(github.getMyself().getLogin().equals(login)) {
                var gitRepo = github.getMyself().getRepository(repositoryName);
                List<Branch> branches = Collections.emptyList();
                try {
                    branches = gitRepo.getBranches().values().stream().map(b ->{
                        Optional<GHTreeEntry> ciFileEntry = Optional.empty();
                        try {
                            ciFileEntry = gitRepo.getTree(b.getSHA1()).getTree().stream().filter(entry -> {
                                logger.debug("entry: " + entry.getPath());
                                if (entry.getPath().equals("ci.json")) {
                                    return true;
                                }
                                return false;
                            }).findFirst();
                        }catch(Exception e){

                        }
                        return new Branch(b.getName(), b.getSHA1(), ciFileEntry.isPresent() ? true : false);
                    }).toList();
                    return ResponseEntity.ok(branches);
                }catch (IOException ex) {
                    logger.debug("ex", ex);
                    return ResponseEntity.status(401).build();
                }
            }else{
                logger.debug(currentLogin + " and " + login );
                return ResponseEntity.status(401).build();
            }
        }catch(IOException e){
            logger.debug("ex", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}

class UpdateRepository{
    boolean enabled;
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

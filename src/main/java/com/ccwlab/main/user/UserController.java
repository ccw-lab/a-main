package com.ccwlab.main.user;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("/{userId}")
    @Operation(description="Get user information of a user id.")
    @ApiResponse(responseCode = "200", description = "A status has been updated successfully.")
    @ApiResponse(responseCode = "404", description = "Requested user information was not found.")
    User getUser(@Parameter(description = "An id where you want to get information") @PathVariable String userId){
        throw new UnsupportedOperationException();
    }

    @GetMapping("/{userId}/repositories")
    @Operation(description="Get a list of user's repositories.")
    @ApiResponse(responseCode = "200", description = "A requested list")
    List<Repository> getRepositories(@PathVariable String userId){
        throw new UnsupportedOperationException();
    }

    @PutMapping("/{userId}/repositories/{repositoryId}")
    @Operation(description="Enable or disable CI/CD on this repository.")
    @ApiResponse(responseCode = "200", description = "This repository has been updated successfully.")
    Repository updateRepository(@Parameter(description = "object containing whether CI/CD is enabled") @RequestBody UpdateRepository repository,
                                @PathVariable String userId,
                                @PathVariable String repositoryId) {
        throw new UnsupportedOperationException();
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
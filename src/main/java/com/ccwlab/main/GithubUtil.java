package com.ccwlab.main;

import com.ccwlab.main.user.User;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubUtil {
    public GitHub get(String accessToken){
        try {
            GitHub github = new GitHubBuilder().withOAuthToken(accessToken).build();
            return github;
        }catch(IOException e){
            return null;
        }
    }
}

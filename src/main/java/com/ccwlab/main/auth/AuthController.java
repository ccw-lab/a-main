package com.ccwlab.main.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.ccwlab.main.JwtUtil;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("auth")
public class AuthController {
    private Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("login")
    ResponseEntity login(@RequestBody Token tokenAndFireId){
        var accessToken= tokenAndFireId.token;
        log.debug(tokenAndFireId.toString());
        try {
            GitHub github = new GitHubBuilder().withOAuthToken(accessToken).build();
            var myself = github.getMyself();
            log.debug(myself.toString());
            var token = this.jwtUtil.getEncodedJwt(accessToken);
            return ResponseEntity.ok().header("Authorization"
                    , "Bearer " + token).build();
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
            log.error("ex", exception);
            return ResponseEntity.internalServerError().build();
        } catch (IOException exception){
            log.error("ex", exception);
            return ResponseEntity.internalServerError().build();
        }
    }
}

class Token{
    String token;
    String fireId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFireId() {
        return fireId;
    }

    public void setFireId(String fireId) {
        this.fireId = fireId;
    }
}

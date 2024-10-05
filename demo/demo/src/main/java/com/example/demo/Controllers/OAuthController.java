package com.example.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class OAuthController {

    @Autowired
    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @GetMapping("/login/oauth2/code/{provider}")
    public ResponseEntity<?> getOauthUserCredentials(
            @PathVariable String provider,
            OAuth2AuthenticationToken auth2AuthenticationToken) {
        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService
                .loadAuthorizedClient(auth2AuthenticationToken.getAuthorizedClientRegistrationId(),
                        auth2AuthenticationToken.getName());

        String providerConf = auth2AuthenticationToken.getAuthorizedClientRegistrationId();

        String email = auth2AuthenticationToken.getPrincipal().getName();

        System.out.println("ProviderConf : ->" + providerConf);

        System.out.println("Email : ->" + email);

        System.out.println("Client : ->" + client);

        return ResponseEntity.ok().body(null);
    }

    // public ResponseEntity<?> getUser

    @GetMapping("/getOauthCredentials")
    public ResponseEntity<?> getCredentials(
            OAuth2AuthenticationToken auth2AuthenticationToken) {

        System.out.println(auth2AuthenticationToken);

        System.out.println(oAuth2AuthorizedClientService.getClass().getName());

        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService
                .loadAuthorizedClient(auth2AuthenticationToken.getAuthorizedClientRegistrationId(),
                        auth2AuthenticationToken.getName());

        String providerConf = auth2AuthenticationToken.getAuthorizedClientRegistrationId();

        OAuth2User user = auth2AuthenticationToken.getPrincipal();

        System.out.println("ProviderConf : ->" + providerConf);

        System.out.println("User : ->" + user);

        System.out.println("Client : ->" + client);

        return ResponseEntity.ok().body(user.getAttributes().get("email"));
    }

}

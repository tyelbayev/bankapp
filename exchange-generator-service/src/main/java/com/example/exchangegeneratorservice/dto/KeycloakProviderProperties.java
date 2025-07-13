package com.example.exchangegeneratorservice.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client.provider.keycloak")
public class KeycloakProviderProperties {
    private String tokenUri;

    public void setTokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
    }

    public String getTokenUri() {
        return tokenUri;
    }
}

package pl.maciejpajak.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

@Configuration
@EnableResourceServer
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    
    @Value("${security.oauth.client-id}")
    private String clientId;

    @Value("${security.oauth.client-secret}")
    private String clientSecret;

    @Value("${security.oauth.grant-type}")
    private String grantType;

    @Value("${security.oauth.scope-read}")
    private String scopeRead;

    @Value("${security.oauth.scope-write}")
    private String scopeWrite = "write";

    @Value("${security.oauth.resource-ids}")
    private String resourceIds;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
    }

//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        
//    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
            .withClient(clientId)
            .authorizedGrantTypes(grantType)
//            .authorizedGrantTypes("password", "refresh_token")
//            .authorities("ROLE_USER")
            .resourceIds(resourceIds)
            .scopes(scopeRead, scopeWrite)
            .secret(clientSecret);
    }

    
}

package org.sanchez.corcoles.ana.pruebasconcepto.oauth.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdditionalInformationToken additionalInformationToken;

    //Configurar los permisos que van a tener nuestros endpoints del servidor de autenticación para generar y validar el token.
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()") //tokenKeyAccess es el endpoint para generar el token, cualquier cliente puede acceder a esta ruta para generar el token
                .checkTokenAccess("isAuthenticated()"); //checkTokenAccess se encarga de validar el token, isAuthenticated() nos permite validar que el cliente esté autenticado.
    }

    /*
     * Existen varios tipos de Grant Types:
     * Password (grant_type = password)
     * Client Credentials (grant_type = client_credentials), se utiliza para autenticación de máquin a a máquina, no se requiere permiso de un usuario específico
     * Implicit (response_type = token), se utiliza para aplicaciones públicas que no requieran mucha seguridad.
     * Authorization Code (response_type = code), se utiliza por lo general en páginas web, es a través de un código de autorización
     * Authorization Code con PKCE, es la solución recomendada para aplicaciones móviles y Single Page Application (SPA).
     * Refresh Token (grant_type = refresh_token), es necesario tener un token anterior.
     * Access Token, suele ser un JWT
     * */

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("frontedapp") //El client_id es el identificador público de la aplicación
                .secret(passwordEncoder.encode("12345")) //El client_secret es una contraseña o secreto que generaremos en el servidor de OAuth en relación con el cliente (la aplicación).
                .scopes("read", "write") //El scope podría ser cualquier valor que ayude a autorizar el uso de nuestras aplicaciones.
                .authorizedGrantTypes("password", "refresh_token") //Como vamos a obtener el token, los campos que corresponden al grant_type password son username, password, client_id, client_secret y scope
                .accessTokenValiditySeconds(3600) //tiempo de validez del token, ponemos 1h de validez
                .refreshTokenValiditySeconds(3600);
    }

    @Override
    //Se encarga de generar el token
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain(); //Para unir la información de los token.
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter(), additionalInformationToken));
        endpoints.authenticationManager(authenticationManager)
                .accessTokenConverter(accessTokenConverter())
                .tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancerChain);
    }

    @Bean("jwtTokenStore")
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    //Es la configuración por defecto del token
    @Bean("jwtAccessTokenConverter")
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("codigo_secreto"); //Esto debe ser un secreto
        return jwtAccessTokenConverter;
    }
}

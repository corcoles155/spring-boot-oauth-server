package org.sanchez.corcoles.ana.pruebasconcepto.oauth.configuration;

import org.sanchez.corcoles.ana.pruebasconcepto.oauth.service.IUsuarioService;
import org.sanchez.corcoles.ana.pruebasconcepto.usuarios.commons.model.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AdditionalInformationToken implements TokenEnhancer {

    @Autowired
    private IUsuarioService usuarioService;

    //Añadir más info a nuestro token
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        final Map<String, Object> additionalInformation = new HashMap<>();
        final Usuario usuario = usuarioService.findByUserName(oAuth2Authentication.getName());
        additionalInformation.put("nombre", usuario.getNombre());
        additionalInformation.put("apellido", usuario.getApellido());
        additionalInformation.put("email", usuario.getEmail());
        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionalInformation);
        return oAuth2AccessToken;
    }
}

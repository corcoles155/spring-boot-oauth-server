package org.sanchez.corcoles.ana.pruebasconcepto.oauth.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);

    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        LOGGER.info("Success login: " + userDetails.getUsername());
    }

    @Override
    public void publishAuthenticationFailure(AuthenticationException e, Authentication authentication) {
        LOGGER.info("Error en el login: " + e.getMessage());
    }
}

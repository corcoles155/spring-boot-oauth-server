package org.sanchez.corcoles.ana.pruebasconcepto.oauth.event;

import feign.FeignException;
import org.sanchez.corcoles.ana.pruebasconcepto.oauth.service.IUsuarioService;
import org.sanchez.corcoles.ana.pruebasconcepto.usuarios.commons.model.entity.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);

    private static final Integer NUMERO_INTENTOS = 3;

    @Autowired
    private IUsuarioService usuarioService;

    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        LOGGER.info("Success login: " + userDetails.getUsername());

        final Usuario usuario = usuarioService.findByUserName(authentication.getName());

        if (usuario.getIntentos() != null && usuario.getIntentos() > 0) {
            LOGGER.info(String.format("Reiniciamos el número de intentos del usuario %s a 0", usuario.getIntentos()));
            usuario.setIntentos(0);
        }
    }

    @Override
    public void publishAuthenticationFailure(AuthenticationException authenticationException, Authentication authentication) {
        LOGGER.info("Error en el login: " + authenticationException.getMessage());

        try {
            final Usuario usuario = usuarioService.findByUserName(authentication.getName());

            if (usuario.getIntentos() == null) {
                usuario.setIntentos(0);
            }

            LOGGER.info(String.format("El número de intentos actual es de %s", usuario.getIntentos()));
            usuario.setIntentos(usuario.getIntentos() + 1);
            LOGGER.info(String.format("El número de intentos después es de %s", usuario.getIntentos()));

            if (usuario.getIntentos() >= NUMERO_INTENTOS) {
                LOGGER.info(String.format("El usuario %s ha sido deshabilitado porque ha superado el número máximo de intentos", authentication.getName()));
                usuario.setEnabled(false);
            }

            usuarioService.update(usuario, usuario.getId());

        } catch (FeignException feignException) {
            LOGGER.info(String.format("El usuario %s no existe en el sistema", authentication.getName()));
        }
    }
}

package org.sanchez.corcoles.ana.pruebasconcepto.oauth.service;

import feign.FeignException;
import org.sanchez.corcoles.ana.pruebasconcepto.oauth.client.UsuarioFeignClient;
import org.sanchez.corcoles.ana.pruebasconcepto.usuarios.commons.model.entity.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements UserDetailsService, IUsuarioService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioFeignClient usuarioFeignClient;

    @Override
    public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException {

        try {
            final Usuario usuario = usuarioFeignClient.findByUserName(userName);

            final List<GrantedAuthority> authorities = usuario.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getNombre())).peek(authority -> LOGGER.info("Rol: " + authority.getAuthority())).collect(Collectors.toList());
            LOGGER.info("Usuario autenticado " + userName);

            return new User(usuario.getUserName(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);

        } catch (FeignException feignException) {
            LOGGER.error("Error en el login, noexiste el usuario " + userName + " en el sistema");
            throw new UsernameNotFoundException("Error en el login, noexiste el usuario " + userName + " en el sistema");
        }
    }

    @Override
    public Usuario findByUserName(final String userName) {
        return usuarioFeignClient.findByUserName(userName);
    }

    @Override
    public Usuario update(final Usuario usuario, final Long id) {
        return usuarioFeignClient.update(usuario, id);
    }
}

package org.sanchez.corcoles.ana.pruebasconcepto.oauth.client;

import org.sanchez.corcoles.ana.pruebasconcepto.usuarios.commons.model.entity.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "servicio-usuarios")
public interface UsuarioFeignClient {

    @GetMapping("/usuarios/search/findByUserName")
    Usuario findByUserName(@RequestParam String username);

}

package org.sanchez.corcoles.ana.pruebasconcepto.oauth.client;

import org.sanchez.corcoles.ana.pruebasconcepto.usuarios.commons.model.entity.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "servicio-usuarios")
public interface UsuarioFeignClient {

    @GetMapping("/usuarios/search/findByUserName")
    Usuario findByUserName(@RequestParam String username);

    @PutMapping("/usuarios/{id}")
    Usuario update(@RequestBody Usuario usuario, @PathVariable Long id);

}

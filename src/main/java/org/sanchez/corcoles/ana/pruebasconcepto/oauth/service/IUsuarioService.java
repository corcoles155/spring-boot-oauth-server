package org.sanchez.corcoles.ana.pruebasconcepto.oauth.service;

import org.sanchez.corcoles.ana.pruebasconcepto.usuarios.commons.model.entity.Usuario;

public interface IUsuarioService {

    Usuario findByUserName(String userName);

    Usuario update(Usuario usuario, Long id);
}

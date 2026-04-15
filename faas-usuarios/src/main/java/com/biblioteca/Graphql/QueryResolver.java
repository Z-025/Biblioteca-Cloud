package com.biblioteca.Graphql;

import com.biblioteca.usuarios.Usuario;
import com.biblioteca.usuarios.UsuariosRepository;
import java.util.List;

public class QueryResolver {
    
    public QueryResolver() {
    }

    public List<Usuario> getUsuarios() {
        return UsuariosRepository.obtenerUsuarios();
    }
}
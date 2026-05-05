package com.biblioteca.graphql;

import com.biblioteca.libros.Libro;
import com.biblioteca.libros.LibrosRepository;
import java.util.List;

public class QueryResolver {
    public List<Libro> getLibros() {
        return LibrosRepository.obtenerLibros();
    }
}
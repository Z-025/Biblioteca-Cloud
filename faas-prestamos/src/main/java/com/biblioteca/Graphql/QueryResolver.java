package com.biblioteca.Graphql;

import com.biblioteca.prestamos.Prestamo;
import com.biblioteca.prestamos.PrestamosRepository;
import java.util.List;

public class QueryResolver {
    
    public QueryResolver() {
    }

    public List<Prestamo> getPrestamos() {
        return PrestamosRepository.obtenerPrestamos();
    }
}
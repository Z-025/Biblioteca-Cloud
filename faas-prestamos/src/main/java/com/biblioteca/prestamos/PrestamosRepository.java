package com.biblioteca.prestamos;

import java.util.ArrayList;
import java.util.List;

public class PrestamosRepository {
    
    public static List<Prestamo> obtenerPrestamos() {
        List<Prestamo> lista = new ArrayList<>();
        // Agregamos datos de prueba a nuestra memoria
        lista.add(new Prestamo(1001, "Cien años de soledad", "Pamela Azúa", "2026-04-08"));
        lista.add(new Prestamo(1002, "Ficciones", "Profesor Valverde", "2026-04-09"));
        return lista;
    }
}
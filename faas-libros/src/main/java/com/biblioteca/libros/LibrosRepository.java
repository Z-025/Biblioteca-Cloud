package com.biblioteca.libros;

import com.biblioteca.oracle.OracleConnectionFactory;
import java.sql.*;
import java.util.*;

public class LibrosRepository {
    public static List<Libro> obtenerLibros() {
        List<Libro> lista = new ArrayList<>();
        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id_libro, titulo FROM LIBROS")) {

            while (rs.next()) {
                lista.add(new Libro(
                    rs.getInt("id_libro"), 
                    rs.getString("titulo")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error conectando a Oracle: " + e.getMessage(), e);
        }
        return lista;
    }
}

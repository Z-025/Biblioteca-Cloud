package com.biblioteca.prestamos;

import com.biblioteca.oracle.OracleConnectionFactory;
import java.sql.*;
import java.util.*;

public class PrestamosRepository {

    // 1. Método original (Para la API REST)
    public static List<Prestamo> obtenerPrestamos() {
        List<Prestamo> lista = new ArrayList<>();
        String query = "SELECT p.id_prestamo, l.titulo, u.nombre, p.fecha_prestamo " +
                       "FROM PRESTAMOS p " +
                       "JOIN USUARIOS u ON p.id_usuario = u.id_usuario " +
                       "JOIN LIBROS l ON p.id_libro = l.id_libro";

        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                lista.add(new Prestamo(
                    rs.getInt("id_prestamo"), rs.getString("titulo"),
                    rs.getString("nombre"), rs.getString("fecha_prestamo")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error conectando a Oracle en Préstamos: " + e.getMessage(), e);
        }
        return lista;
    }

    // 2. NUEVO MÉTODO PARA GRAPHQL (Búsqueda por ID)
    public static Prestamo obtenerPrestamoPorId(int id) {
        String query = "SELECT p.id_prestamo, l.titulo, u.nombre, p.fecha_prestamo " +
                       "FROM PRESTAMOS p " +
                       "JOIN USUARIOS u ON p.id_usuario = u.id_usuario " +
                       "JOIN LIBROS l ON p.id_libro = l.id_libro " +
                       "WHERE p.id_prestamo = ?";
        
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id); // Filtramos por el ID enviado desde GraphQL
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Prestamo(
                        rs.getInt("id_prestamo"), rs.getString("titulo"),
                        rs.getString("nombre"), rs.getString("fecha_prestamo")
                    );
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error buscando préstamo por ID: " + e.getMessage(), e);
        }
        return null;
    }
}
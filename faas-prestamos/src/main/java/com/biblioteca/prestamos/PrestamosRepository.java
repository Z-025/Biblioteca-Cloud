package com.biblioteca.prestamos;

import com.biblioteca.oracle.OracleConnectionFactory;
import java.sql.*;
import java.util.*;

public class PrestamosRepository {

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

    public static Prestamo obtenerPrestamoPorId(int id) {
        String query = "SELECT p.id_prestamo, l.titulo, u.nombre, p.fecha_prestamo " +
                       "FROM PRESTAMOS p " +
                       "JOIN USUARIOS u ON p.id_usuario = u.id_usuario " +
                       "JOIN LIBROS l ON p.id_libro = l.id_libro " +
                       "WHERE p.id_prestamo = ?";
        
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
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

    public static boolean crearPrestamo(int idPrestamo, int idUsuario, int idLibro) {
        String query = "INSERT INTO PRESTAMOS (id_prestamo, id_usuario, id_libro, fecha_prestamo) VALUES (?, ?, ?, SYSDATE)";
        
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, idPrestamo);
            pstmt.setInt(2, idUsuario);
            pstmt.setInt(3, idLibro);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (Exception e) {
            throw new RuntimeException("Error insertando préstamo en Oracle: " + e.getMessage(), e);
        }
    }
}
package com.biblioteca.usuarios;

import com.biblioteca.oracle.OracleConnectionFactory;
import java.sql.*;
import java.util.*;

public class UsuariosRepository {


    public static List<Usuario> obtenerUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = OracleConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id_usuario, nombre, email FROM USUARIOS")) {

            while (rs.next()) {
                lista.add(new Usuario(
                    rs.getInt("id_usuario"), 
                    rs.getString("nombre"), 
                    rs.getString("email")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error conectando a Oracle: " + e.getMessage(), e);
        }
        return lista;
    }

    // NUEVO MÉTODO PARA GRAPHQL: Busca un solo usuario por su ID
    public static Usuario obtenerUsuarioPorId(int id) {
        String query = "SELECT id_usuario, nombre, email FROM USUARIOS WHERE id_usuario = ?";
        
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id); 
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getInt("id_usuario"), 
                        rs.getString("nombre"), 
                        rs.getString("email")
                    );
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error buscando usuario por ID: " + e.getMessage(), e);
        }
        return null; 
    }
}
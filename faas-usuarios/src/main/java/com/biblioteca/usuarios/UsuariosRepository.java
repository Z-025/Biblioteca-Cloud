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
}
package com.biblioteca.usuarios;

import java.io.*;
import java.sql.*;
import java.util.*;

public class UsuariosRepository {
    
    private static Connection getConnection() throws Exception {
        String base64Wallet = System.getenv("DB_WALLET_BASE64");
        String dbUrl = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPass = System.getenv("DB_PASSWORD");
        
        // Creamos un archivo temporal para el Wallet
        File walletFile = File.createTempFile("wallet", ".zip");
        byte[] decodedBytes = Base64.getDecoder().decode(base64Wallet.trim());
        try (FileOutputStream fos = new FileOutputStream(walletFile)) {
            fos.write(decodedBytes);
        }

        // La URL debe apuntar al archivo temporal
        String urlWithWallet = dbUrl + "?TNS_ADMIN=" + walletFile.getParent();
        return DriverManager.getConnection(urlWithWallet, dbUser, dbPass);
    }

    public static List<Usuario> obtenerUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id_usuario, nombre, email FROM USUARIOS")) {

            while (rs.next()) {
                lista.add(new Usuario(rs.getInt("id_usuario"), rs.getString("nombre"), rs.getString("email")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
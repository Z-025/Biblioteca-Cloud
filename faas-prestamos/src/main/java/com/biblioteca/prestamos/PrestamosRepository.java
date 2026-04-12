package com.biblioteca.prestamos;

import java.io.*;
import java.sql.*;
import java.util.*;

public class PrestamosRepository {

    private static Connection getConnection() throws Exception {
        String base64Wallet = System.getenv("DB_WALLET_BASE64");
        String dbUrl = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPass = System.getenv("DB_PASSWORD");
        
        File walletFile = File.createTempFile("wallet_p", ".zip");
        byte[] decodedBytes = Base64.getDecoder().decode(base64Wallet.trim());
        try (FileOutputStream fos = new FileOutputStream(walletFile)) {
            fos.write(decodedBytes);
        }

        String urlWithWallet = dbUrl + "?TNS_ADMIN=" + walletFile.getParent();
        return DriverManager.getConnection(urlWithWallet, dbUser, dbPass);
    }

    public static List<Prestamo> obtenerPrestamos() {
        List<Prestamo> lista = new ArrayList<>();
        String query = "SELECT p.id_prestamo, l.titulo, u.nombre, p.fecha_prestamo " +
                       "FROM PRESTAMOS p " +
                       "JOIN USUARIOS u ON p.id_usuario = u.id_usuario " +
                       "JOIN LIBROS l ON p.id_libro = l.id_libro";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                lista.add(new Prestamo(
                    rs.getInt("id_prestamo"),
                    rs.getString("titulo"),
                    rs.getString("nombre"),
                    rs.getString("fecha_prestamo")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
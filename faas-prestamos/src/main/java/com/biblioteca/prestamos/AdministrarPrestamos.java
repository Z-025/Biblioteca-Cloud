package com.biblioteca.prestamos;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.sql.*;
import java.util.Optional;

public class AdministrarPrestamos {

    private static final Gson gson = new Gson();

    @FunctionName("AdministrarPrestamos")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        String dbUrl = System.getenv().getOrDefault("DB_URL", "jdbc:oracle:thin:@//localhost:1521/XE");
        String dbUser = System.getenv().getOrDefault("DB_USER", "system");
        String dbPassword = System.getenv().getOrDefault("DB_PASSWORD", "TuPassword123");

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            // CREATE: Registrar un préstamo
            String body = request.getBody().orElse("{}");
            JsonObject jsonBody = gson.fromJson(body, JsonObject.class);
            
            String query = "INSERT INTO PRESTAMOS (id_prestamo, id_usuario, id_libro, fecha_prestamo) VALUES (?, ?, ?, SYSDATE)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, jsonBody.get("id_prestamo").getAsInt());
            pstmt.setInt(2, jsonBody.get("id_usuario").getAsInt());
            pstmt.setInt(3, jsonBody.get("id_libro").getAsInt());
            pstmt.executeUpdate();

            JsonObject response = new JsonObject();
            response.addProperty("mensaje", "Préstamo registrado exitosamente");
            return request.createResponseBuilder(HttpStatus.CREATED).header("Content-Type", "application/json").body(gson.toJson(response)).build();

        } catch (Exception e) {
            JsonObject error = new JsonObject();
            error.addProperty("error", e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).header("Content-Type", "application/json").body(gson.toJson(error)).build();
        }
    }
}
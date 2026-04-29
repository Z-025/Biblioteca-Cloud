package com.biblioteca.prestamos;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class PrestamosRest {
    
    @FunctionName("ObtenerPrestamosREST")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS, route = "rest/prestamos") HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        context.getLogger().info("Ejecutando API REST de Préstamos desde Oracle");

        return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(PrestamosRepository.obtenerPrestamos())
                .build();
    }

    @FunctionName("CrearPrestamo")
    public HttpResponseMessage crear(
            @HttpTrigger(name = "reqPOST", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS, route = "rest/prestamos") HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        context.getLogger().info("Iniciando creación de préstamo...");

        try {
            String body = request.getBody().orElse("");
            JsonObject json = new Gson().fromJson(body, JsonObject.class);
            int idPrestamo = json.get("id_prestamo").getAsInt();
            int idUsuario = json.get("id_usuario").getAsInt();
            int idLibro = json.get("id_libro").getAsInt();

            boolean insertado = PrestamosRepository.crearPrestamo(idPrestamo, idUsuario, idLibro);
            
            if (insertado) {
                enviarEventoAzure(idPrestamo, context);
                return request.createResponseBuilder(HttpStatus.OK).body("Préstamo creado y evento emitido exitosamente.").build();
            } else {
                return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo insertar en la BD.").build();
            }

        } catch (Exception e) {
            context.getLogger().severe("Error: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Error en el formato o base de datos: " + e.getMessage()).build();
        }
    }

    private void enviarEventoAzure(int idPrestamo, ExecutionContext context) throws Exception {
        String endpoint = System.getenv("EVENT_GRID_ENDPOINT");
        String key = System.getenv("EVENT_GRID_KEY");

        String jsonEvent = String.format("[{" +
                "\"id\": \"%s\"," +
                "\"eventType\": \"PrestamoCreado\"," +
                "\"subject\": \"Biblioteca/Prestamos\"," +
                "\"eventTime\": \"%s\"," +
                "\"data\": {\"mensaje\": \"Se creo un nuevo prestamo\", \"id_prestamo\": %d}," +
                "\"dataVersion\": \"1.0\"" +
                "}]", UUID.randomUUID().toString(), Instant.now().toString(), idPrestamo);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("aeg-sas-key", key)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonEvent))
                .build();

        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
        context.getLogger().info("Evento enviado a Event Grid. Status: " + response.statusCode());
    }
}
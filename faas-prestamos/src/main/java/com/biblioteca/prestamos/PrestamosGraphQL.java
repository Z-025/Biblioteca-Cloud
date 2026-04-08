package com.biblioteca.prestamos;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import com.google.gson.Gson;
import java.util.*;

public class PrestamosGraphQL {

    @FunctionName("GraphQLPrestamos")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS, route = "graphql/prestamos") HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        context.getLogger().info("Ejecutando API GraphQL de Préstamos (Mock)");

        String body = request.getBody().orElse("").toLowerCase();
        Gson gson = new Gson();
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        // Si la petición contiene la palabra "prestamos", devolvemos la lista
        if (body.contains("prestamos")) {
            data.put("prestamos", PrestamosRepository.obtenerPrestamos());
        }

        response.put("data", data);

        return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(gson.toJson(response))
                .build();
    }
}
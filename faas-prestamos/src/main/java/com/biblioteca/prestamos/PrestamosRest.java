package com.biblioteca.prestamos;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import java.util.Optional;

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
}
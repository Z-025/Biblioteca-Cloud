package com.biblioteca.usuarios;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import java.util.Optional;

public class UsuariosRest {
    
    @FunctionName("ObtenerUsuariosREST")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS, route = "rest/usuarios") HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        context.getLogger().info("Ejecutando API REST de Usuarios (Mock)");

        return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(UsuariosRepository.obtenerUsuarios())
                .build();
    }
}
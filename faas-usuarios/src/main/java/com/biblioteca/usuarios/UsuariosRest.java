package com.biblioteca.usuarios;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import com.google.gson.Gson; // 1. Tienes que importar Gson
import java.util.Optional;

public class UsuariosRest {
    
    @FunctionName("ObtenerUsuariosREST")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS, route = "rest/usuarios") HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        context.getLogger().info("Ejecutando API REST de Usuarios desde Oracle"); 

        return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(new Gson().toJson(UsuariosRepository.obtenerUsuarios())) 
                .build();
    }
}
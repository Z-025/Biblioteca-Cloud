package main.java.com.biblioteca.auditoria;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

public class AuditoriaEventGrid {

    @FunctionName("ProcesarNuevoPrestamo")
    public void run(
        @EventGridTrigger(name = "event") String event,
        final ExecutionContext context
    ) {
        context.getLogger().info("=== EVENTO RECIBIDO DESDE EVENT GRID ===");
        context.getLogger().info("Datos del evento: " + event);

        context.getLogger().info("Préstamo registrado correctamente");
    }
}
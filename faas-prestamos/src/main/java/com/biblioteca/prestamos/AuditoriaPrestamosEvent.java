package com.biblioteca.prestamos;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

public class AuditoriaPrestamosEvent {

    @FunctionName("AuditoriaPrestamos")
    public void run(
            @EventGridTrigger(name = "event") String eventData,
            final ExecutionContext context) {
        
        context.getLogger().info("=== NUEVO EVENTO RECIBIDO EN SEGUNDO PLANO ===");
        context.getLogger().info("Datos del evento procesado: " + eventData);
        context.getLogger().info("Notificación/Auditoría registrada exitosamente.");
    }
}
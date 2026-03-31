package com.biblioteca.bff_biblioteca;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/biblioteca")
public class BibliotecaController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String urlFaaSUsuarios = System.getenv().getOrDefault("URL_FAAS_USUARIOS", "http://localhost:7071/api/AdministrarUsuarios");

    @GetMapping("/usuarios")
    public ResponseEntity<String> obtenerUsuarios() {
        ResponseEntity<String> response = restTemplate.getForEntity(urlFaaSUsuarios, String.class);
        return ResponseEntity.ok(response.getBody());
    }
}
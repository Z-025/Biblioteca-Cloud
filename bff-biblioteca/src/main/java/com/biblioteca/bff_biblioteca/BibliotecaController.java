package com.biblioteca.bff_biblioteca;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/bff")
public class BibliotecaController {

    private final RestTemplate restTemplate = new RestTemplate();

    // Las URLs se inyectan desde el docker-compose
    @Value("${URL_FAAS_USUARIOS:http://localhost:7071/api/AdministrarUsuarios}")
    private String urlFaaSUsuarios;

    @Value("${URL_FAAS_PRESTAMOS:http://localhost:7072/api/AdministrarPrestamos}")
    private String urlFaaSPrestamos;

    // --- ORQUESTAR USUARIOS ---
    @GetMapping("/usuarios")
    public ResponseEntity<String> listarUsuarios() {
        ResponseEntity<String> response = restTemplate.getForEntity(urlFaaSUsuarios, String.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    @PostMapping("/usuarios")
    public ResponseEntity<String> crearUsuario(@RequestBody String usuarioJson) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(usuarioJson, headers);
        
        ResponseEntity<String> response = restTemplate.postForEntity(urlFaaSUsuarios, request, String.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    // --- ORQUESTAR PRÉSTAMOS ---
    @PostMapping("/prestamos")
    public ResponseEntity<String> crearPrestamo(@RequestBody String prestamoJson) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(prestamoJson, headers);
        
        ResponseEntity<String> response = restTemplate.postForEntity(urlFaaSPrestamos, request, String.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}
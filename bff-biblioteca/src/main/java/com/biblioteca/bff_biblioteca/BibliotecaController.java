package com.biblioteca.bff_biblioteca;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/bff")
@CrossOrigin(origins = "*")
public class BibliotecaController {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String URL_CREAR_PRESTAMO = "https://faas-prestamos-1774920964372.azurewebsites.net/api/rest/prestamos";
    private final String URL_LISTAR_PRESTAMOS = "https://faas-prestamos-1774920964372.azurewebsites.net/api/rest/prestamos";
    private final String URL_USUARIOS = "https://faas-usuarios2024.azurewebsites.net/api/graphql/usuarios"; 
    private final String URL_LIBROS = "https://faas-libros2026.azurewebsites.net/api/graphql/libros";

    @PostMapping("/prestamos")
    public ResponseEntity<String> crearPrestamo(@RequestBody String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        return restTemplate.exchange(URL_CREAR_PRESTAMO, HttpMethod.POST, request, String.class);
    }


    @GetMapping("/prestamos")
    public ResponseEntity<String> listarPrestamos() {
        return restTemplate.getForEntity(URL_LISTAR_PRESTAMOS, String.class);
    }

    @PostMapping("/usuarios/graphql")
    public ResponseEntity<String> graphqlUsuarios(@RequestBody String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(query, headers);
        
        return restTemplate.exchange(URL_USUARIOS, HttpMethod.POST, request, String.class);
    }

    @PostMapping("/libros/graphql")
    public ResponseEntity<String> graphqlLibros(@RequestBody String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(query, headers);
        
        return restTemplate.exchange(URL_LIBROS, HttpMethod.POST, request, String.class);
    }
}
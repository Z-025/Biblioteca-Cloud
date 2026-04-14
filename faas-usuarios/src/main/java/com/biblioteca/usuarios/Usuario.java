package com.biblioteca.usuarios;

public class Usuario {
    private int id_usuario;
    private String nombre;
    private String email;

    public Usuario(int id_usuario, String nombre, String email) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.email = email;
    }
}
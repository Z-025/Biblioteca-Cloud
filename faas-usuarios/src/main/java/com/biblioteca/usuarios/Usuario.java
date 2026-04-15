package com.biblioteca.usuarios;

public class Usuario {
    public int id_usuario;
    public String nombre;
    public String email;

    public Usuario(int id_usuario, String nombre, String email) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.email = email;
    }
}
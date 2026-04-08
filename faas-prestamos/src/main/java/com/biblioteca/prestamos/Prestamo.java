package com.biblioteca.prestamos;

public class Prestamo {
    public int id_prestamo;
    public String titulo_libro;
    public String nombre_usuario;
    public String fecha;

    public Prestamo(int id_prestamo, String titulo_libro, String nombre_usuario, String fecha) {
        this.id_prestamo = id_prestamo;
        this.titulo_libro = titulo_libro;
        this.nombre_usuario = nombre_usuario;
        this.fecha = fecha;
    }
}
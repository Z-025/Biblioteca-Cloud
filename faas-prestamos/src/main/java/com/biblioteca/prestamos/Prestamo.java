package com.biblioteca.prestamos;

public class Prestamo {
    public int id_prestamo;
    public String titulo;
    public String nombre;
    public String fecha_prestamo;

    public Prestamo(int id_prestamo, String titulo, String nombre, String fecha_prestamo) {
        this.id_prestamo = id_prestamo;
        this.titulo = titulo;
        this.nombre = nombre;
        this.fecha_prestamo = fecha_prestamo;
    }
}
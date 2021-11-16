package com.example.hotel_ya.objetos;

public class Hotel {
    private String nombre_hotel;
    private String telefono;
    private String correo;
    private String direccion;
    private String num_estrellas;
    private String descripcion;
    private String status;
    private String []comentarios;
    private String []habitaciones;

    public Hotel(String nombre_hotel, String telefono, String correo, String direccion, String num_estrellas, String descripcion, String status, String[] comentarios, String[] habitaciones) {
        this.nombre_hotel = nombre_hotel;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.num_estrellas = num_estrellas;
        this.descripcion = descripcion;
        this.status = status;
        this.comentarios = comentarios;
        this.habitaciones = habitaciones;
    }

    public Hotel() {
    }

    public String getNombre_hotel() {
        return nombre_hotel;
    }

    public void setNombre_hotel(String nombre_hotel) {
        this.nombre_hotel = nombre_hotel;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNum_estrellas() {
        return num_estrellas;
    }

    public void setNum_estrellas(String num_estrellas) {
        this.num_estrellas = num_estrellas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getComentarios() {
        return comentarios;
    }

    public void setComentarios(String[] comentarios) {
        this.comentarios = comentarios;
    }

    public String[] getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(String[] habitaciones) {
        this.habitaciones = habitaciones;
    }
}

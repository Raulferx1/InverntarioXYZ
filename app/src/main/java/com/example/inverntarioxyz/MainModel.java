package com.example.inverntarioxyz;

public class MainModel {
    String Nombre, Marca, Descripcion, imgURL;

    public MainModel() {

    }

    public MainModel(String nombre, String marca, String descripcion, String imgURL) {
        this.Nombre = nombre;
        this.Marca = marca;
        this.Descripcion = descripcion;
        this.imgURL = imgURL;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String marca) {
        this.Marca = marca;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.Descripcion = descripcion;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}



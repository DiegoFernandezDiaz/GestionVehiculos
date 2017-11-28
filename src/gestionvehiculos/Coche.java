/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionvehiculos;

import java.util.Date;

/**
 *
 * @author usuario
 */
public class Coche 
{
    private int cocheId,cilindrada;
    private String matricula,marca,modelo;
    private Date FechaMatriculacion;

    public Coche(int cocheId, int cilindrada, String matricula, String marca, String modelo, Date FechaMatriculacion) {
        this.cocheId = cocheId;
        this.cilindrada = cilindrada;
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.FechaMatriculacion = FechaMatriculacion;
    }

    public int getCocheId() {
        return cocheId;
    }

    public void setCocheId(int cocheId) {
        this.cocheId = cocheId;
    }

    public int getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(int cilindrada) {
        this.cilindrada = cilindrada;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Date getFechaMatriculacion() {
        return FechaMatriculacion;
    }

    public void setFechaMatriculacion(Date FechaMatriculacion) {
        this.FechaMatriculacion = FechaMatriculacion;
    }

    @Override
    public String toString() {
        return "Coche{" + "cocheId=" + cocheId + ", cilindrada=" + cilindrada + ", matricula=" + matricula + ", marca=" + marca + ", modelo=" + modelo + ", FechaMatriculacion=" + FechaMatriculacion + '}';
    }
    
    
}

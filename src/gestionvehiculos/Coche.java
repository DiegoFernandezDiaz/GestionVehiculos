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
    private int cocheId;
    private String matricula;
    private String marca;
    private String modelo;
    private String extras;
    private int cilintrada;
    private int año;
    private String numBastidor;
    private int precioVenta;

    public Coche() {
    }

    public Coche(int cocheId, String matricula, String marca, String modelo, String extras, int cilintrada, int año, String numBastidor, int precioVenta) {
        this.cocheId = cocheId;
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.extras = extras;
        this.cilintrada = cilintrada;
        this.año = año;
        this.numBastidor = numBastidor;
        this.precioVenta = precioVenta;
    }

    public int getCocheId() {
        return cocheId;
    }

    public void setCocheId(int cocheId) {
        this.cocheId = cocheId;
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

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public int getCilintrada() {
        return cilintrada;
    }

    public void setCilintrada(int cilintrada) {
        this.cilintrada = cilintrada;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public String getNumBastidor() {
        return numBastidor;
    }

    public void setNumBastidor(String numBastidor) {
        this.numBastidor = numBastidor;
    }

    public int getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(int precioVenta) {
        this.precioVenta = precioVenta;
    }

    @Override
    public String toString() {
        return "Coche{" + "cocheId=" + cocheId + ", matricula=" + matricula + ", marca=" + marca + ", modelo=" + modelo + ", extras=" + extras + ", cilintrada=" + cilintrada + ", a\u00f1o=" + año + ", numBastidor=" + numBastidor + ", precioVenta=" + precioVenta + '}';
    }

    
    
    
    
}

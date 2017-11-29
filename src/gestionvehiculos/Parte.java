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
public class Parte {
    private int parteId;
    private String codigo;
    private Date Fecha;
    private Coche cocheId;

    public Parte() {
    }

    public Parte(int parteId, String codigo, Date Fecha, Coche cocheId) {
        this.parteId = parteId;
        this.codigo = codigo;
        this.Fecha = Fecha;
        this.cocheId = cocheId;
    }

    public int getParteId() {
        return parteId;
    }

    public void setParteId(int parteId) {
        this.parteId = parteId;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date Fecha) {
        this.Fecha = Fecha;
    }

    public Coche getCocheId() {
        return cocheId;
    }

    public void setCocheId(Coche cocheId) {
        this.cocheId = cocheId;
    }

    @Override
    public String toString() {
        return "Parte{" + "parteId=" + parteId + ", codigo=" + codigo + ", Fecha=" + Fecha + ", cocheId=" + cocheId + '}';
    }

    
    
}

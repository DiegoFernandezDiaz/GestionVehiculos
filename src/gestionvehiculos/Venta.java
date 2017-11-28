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
public class Venta {
    private int ventaId;
    private Date FechaCompra;
    private Coche cocheId;

    public Venta() {
    }

    public Venta(int ventaId, Date FechaCompra, Coche cocheId) {
        this.ventaId = ventaId;
        this.FechaCompra = FechaCompra;
        this.cocheId = cocheId;
    }

    public int getVentaId() {
        return ventaId;
    }

    public void setVentaId(int ventaId) {
        this.ventaId = ventaId;
    }

    public Date getFechaCompra() {
        return FechaCompra;
    }

    public void setFechaCompra(Date FechaCompra) {
        this.FechaCompra = FechaCompra;
    }

    public Coche getCocheId() {
        return cocheId;
    }

    public void setCocheId(Coche cocheId) {
        this.cocheId = cocheId;
    }

    @Override
    public String toString() {
        return "Venta{" + "ventaId=" + ventaId + ", FechaCompra=" + FechaCompra + ", cocheId=" + cocheId + '}';
    }
    
    
}

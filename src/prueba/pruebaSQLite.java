/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prueba;

import gestionvehiculos.Coche;
import gestionvehiculos.ExcepcionGestionVehiculos;
import gestionvehiculos.GestionVehiculosFactory;
import gestionvehiculos.GestionVehiculosInterface;
import gestionvehiculos.Parte;

/**
 *
 * @author DAM208
 */
public class pruebaSQLite {
    public static void main(String[] args) {
    
        GestionVehiculosInterface gf;
        try {
            gf = GestionVehiculosFactory.getInstance("SQLite");
            Coche c = new Coche(1, "1653WBS","FORD", "FIESTA", "GPS", 1900, 2016, "sdYYav1234ds32fsdA", 140);
            Parte p = new Parte(1, "7", java.sql.Date.valueOf("2017-1-28"), c);
        System.out.println(gf.leerCoche(1));
        } catch (ExcepcionGestionVehiculos ex) {
            System.out.println(ex.getMensajeErrorUsuario());
        }
        
    }
}

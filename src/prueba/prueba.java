/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prueba;

import gestionvehiculos.Coche;
import gestionvehiculos.ExcepcionGestionVehiculos;
import gestionvehiculos.GestionVehiculos;
import gestionvehiculos.Parte;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HARRY
 */
public class prueba {
    
    public static void main(String[] args) {
    GestionVehiculos gs;
        try {
            gs = new GestionVehiculos();
            Coche c = new Coche(1, "1233WBS", null, "FIESTA", "GPS", 1900, 2016, "sdYav1234ds32fsd", 1540);
            Parte p = new Parte(1, "7", java.sql.Date.valueOf("2017-1-28"), c);
        System.out.println(gs.insertarCoche(c));
        } catch (ExcepcionGestionVehiculos ex) {
            System.out.println(ex.getSentenciaSQL());
        }
        
    }
}

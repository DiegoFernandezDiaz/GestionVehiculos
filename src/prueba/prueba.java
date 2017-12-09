/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prueba;

import gestionvehiculos.Coche;
import gestionvehiculos.ExcepcionGestionVehiculos;
import gestionvehiculos.GestionVehiculos;

/**
 *
 * @author HARRY
 */
public class prueba {
    
    public static void main(String[] args) throws ExcepcionGestionVehiculos {
    GestionVehiculos gs = new GestionVehiculos();
        Coche c = new Coche(0, "1234ABS", "FORD", "FIESTA", "GPS", 1900, 2016, "adcfsv1234ds32fsd", 1540);
        System.out.println(gs.insertarCoche(c));
    }
}

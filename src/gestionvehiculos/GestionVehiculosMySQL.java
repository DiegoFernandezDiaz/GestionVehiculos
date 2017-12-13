/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionvehiculos;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author DAM208
 */
public class GestionVehiculosMySQL implements GestionVehiculosInterface {

    public static GestionVehiculosMySQL instance = new GestionVehiculosMySQL();
    
    private GestionVehiculosMySQL(){
        
    }
    
    @Override
    public int insertarCoche(Coche coche) throws ExcepcionGestionVehiculos {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int eliminarCoche(int cocheId) throws ExcepcionGestionVehiculos {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int modificarCoche(int cocheId, Coche coche) throws ExcepcionGestionVehiculos {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Coche leerCoche(int cocheId) throws ExcepcionGestionVehiculos {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Coche> leerCoches() throws ExcepcionGestionVehiculos {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Coche> leerCoches(String dql) throws ExcepcionGestionVehiculos {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Coche> leerCoches(String matricula, String marca, String modelo, String numBastidor, Integer criterioOrden, Integer orden) throws ExcepcionGestionVehiculos {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int insertarParte(Parte parte) throws ExcepcionGestionVehiculos {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int modificarParte(int parteId, Parte parte) throws ExcepcionGestionVehiculos {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int eliminarParte(int parteId) throws ExcepcionGestionVehiculos {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Parte leerParte(int parteId) throws ExcepcionGestionVehiculos {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Parte> leerPartes() throws ExcepcionGestionVehiculos {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Parte> leerParte(String dql) throws ExcepcionGestionVehiculos {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Parte> leerPartes(String codigo, Date fecha, Integer cocheId, Integer criterioOrden, Integer orden) throws ExcepcionGestionVehiculos {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionvehiculos;

/**
 *
 * @author DAM208
 */
public class GestionVehiculosFactory {
    public static GestionVehiculosInterface getInstance(String fuenteDatos){
        if(fuenteDatos.equals("Oracle")) return GestionVehiculosOracle.instance;
        if(fuenteDatos.equals("MySQL")) return GestionVehiculosMySQL.instance;
        if(fuenteDatos.equals("SQLite")) return GestionVehiculosSQLite.instance;
        return null;
    }
}

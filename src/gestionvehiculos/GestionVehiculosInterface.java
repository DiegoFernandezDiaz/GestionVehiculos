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
public interface GestionVehiculosInterface {
        
    public static Integer ASCENDENTE = 1;
    public static Integer DESCENDENTE = 2;
    public static Integer COCHE_MATRICULA = 10;
    public static Integer COCHE_NUMERO_BASTIDOR = 11;
    public static Integer COCHE_MARCA = 12;
    public static Integer COCHE_MODELO = 13;
    public static Integer PARTE_CODIGO = 14;
    public static Integer PARTE_FECHA = 15;
    public static Integer PARTE_COCHE = 16;
    
    /**
     * Inserta un coche en la base de datos.
     * @author Diego Fernández Díaz
     * @param coche coche a insertar
     * @return Cantidad de coches que se han insertado
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public int insertarCoche(Coche coche) throws ExcepcionGestionVehiculos;
    
    /**
     * Elimina un coche de la base de datos
     * @author Diego Fernández Díaz
     * @param cocheId Identificador del coche a eliminar
     * @return Cantidad de coches eliminados
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public int eliminarCoche(int cocheId) throws ExcepcionGestionVehiculos;
    
    /**
     * Modifica un coche de la base de datos
     * @author Diego Fernández Díaz
     * @param cocheId Identificador del coche a modificar
     * @param coche Nuevos datos del coche a modificar
     * @return Cantidad de coches modificados
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public int modificarCoche(int cocheId, Coche coche) throws ExcepcionGestionVehiculos;
    
    /**
     * Consulta un coche de la base de datos
     * @author Diego Fernández Díaz
     * @param cocheId Identificador del coche a consultar
     * @return Coche a consultar
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public Coche leerCoche(int cocheId) throws ExcepcionGestionVehiculos;
    
    /**
     * Consulta todos los coches de la base de datos
     * @author Diego Fernández Díaz
     * @return Lista de todos los coches
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public ArrayList<Coche> leerCoches() throws ExcepcionGestionVehiculos;
    /**
     * Consulta todos los coches de la base de datos
     * @author Diego Fernández Díaz
     * @param dql
     * @return Lista de todos los coches
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public ArrayList<Coche> leerCoches(String dql) throws ExcepcionGestionVehiculos;
    
    /**
     * Consulta todos los coches de la base de datos
     * @author Diego Fernández Díaz
     * @param matricula  matricula del coche
     * @param marca  marca del coche
     * @param modelo modelo del coche
     * @param numBastidor numero del bastidor del coche 
     * @param criterioOrden incica con que criterio se va a ordenar
     * @param orden numero que ordena ascendente o descendente
     * @return Lista de todos los coches
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public ArrayList<Coche> leerCoches(String matricula,String marca,String modelo,
                                        String numBastidor, Integer criterioOrden, Integer orden) throws ExcepcionGestionVehiculos;
    
    /**
    * Inserta un parte de la base de datos
    * @author Diego Fernández Díaz
    * @param parte Nuevos datos del parte a insertar
    * @return Cantidad de partes modificados
    * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
    */
    public int insertarParte(Parte parte) throws ExcepcionGestionVehiculos;
    
    /**
     * Modifica un parte de la base de datos
     * @author Diego Fernández Díaz
     * @param parteId Identificador del parte a modificar
     * @param parte Nuevos datos del parte a modificar
     * @return Cantidad de partes modificados
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public int modificarParte(int parteId, Parte parte) throws ExcepcionGestionVehiculos;
    
    /**
     * Elimina un parte de la base de datos
     * @author Diego Fernández Díaz
     * @param parteId Identificador del parte a eliminar
     * @return Cantidad de partes eliminados
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public int eliminarParte(int parteId) throws ExcepcionGestionVehiculos;
    
    /**
     * Consulta un parte de la base de datos
     * @author Diego Fernández Díaz
     * @param parteId Identificador del parte a consultar
     * @return parte a consultar
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public Parte leerParte(int parteId) throws ExcepcionGestionVehiculos;
    
    /**
     * Consulta todos los partes de la base de datos
     * @author Diego Fernández Díaz
     * @return Lista de todos los partes
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public ArrayList<Parte> leerPartes() throws ExcepcionGestionVehiculos;
    
    /**
     * Consulta todos los partes de la base de datos
     * @author Diego Fernández Díaz
     * @param dql
     * @return Lista de todos los partes
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public ArrayList<Parte> leerParte(String dql) throws ExcepcionGestionVehiculos;
    
    /**
     * Consulta todos los partes de la base de datos
     * @author Diego Fernández Díaz
     * @param codigo codigo del parte
     * @param fecha fecha del parte
     * @param cocheId  id del coche
     * @param criterioOrden incica con que criterio se va a ordenar
     * @param orden numero que ordena ascendente o descendente
     * @return Lista de todos los partes
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public ArrayList<Parte> leerPartes(String codigo,Date fecha,Integer cocheId,Integer criterioOrden, Integer orden) throws ExcepcionGestionVehiculos;
}


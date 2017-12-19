/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionvehiculos;

import static gestionvehiculos.GestionVehiculosInterface.ASCENDENTE;
import static gestionvehiculos.GestionVehiculosInterface.COCHE_MARCA;
import static gestionvehiculos.GestionVehiculosInterface.COCHE_MATRICULA;
import static gestionvehiculos.GestionVehiculosInterface.COCHE_MODELO;
import static gestionvehiculos.GestionVehiculosInterface.COCHE_NUMERO_BASTIDOR;
import static gestionvehiculos.GestionVehiculosInterface.DESCENDENTE;
import static gestionvehiculos.GestionVehiculosInterface.PARTE_COCHE;
import static gestionvehiculos.GestionVehiculosInterface.PARTE_CODIGO;
import static gestionvehiculos.GestionVehiculosInterface.PARTE_FECHA;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author DAM208
 */
public class GestionVehiculosSQLite implements GestionVehiculosInterface{

    public static GestionVehiculosSQLite instance = new GestionVehiculosSQLite();
    
    private Connection conexion;
    
    private GestionVehiculosSQLite(){
        
    }
    /**
     * Abre la conexión con la base de datos 
     * @author Diego Fernández Díaz
     */
    private void abrirConexion() throws ExcepcionGestionVehiculos {
        try {
            Class.forName("org.sqlite.JDBC");
            conexion = DriverManager.getConnection("jdbc:sqlite:bd/aseguradora.db");
        } catch (SQLException ex) {
            ExcepcionGestionVehiculos e = new ExcepcionGestionVehiculos(
                    ex.getErrorCode(),
                    ex.getMessage(),
                    null,
                    null);
            throw e;
        } catch (ClassNotFoundException ex) {
            ExcepcionGestionVehiculos e = new ExcepcionGestionVehiculos(
                    -1,
                    ex.getMessage(),
                    "Error general del sistema. Consulte con el administrador",
                    null);
            throw e;
        }
    }
    /**
     * Cierra de forma ordenada un objeto Statement y la conexión de la base de 
     * datos
     * @author Diego Fernández Díaz
     * @param conexion Conexion a cerrar
     */
    private void cerrarConexion(Connection conexion, PreparedStatement sentenciaPreparada) {
        try {
           sentenciaPreparada.close();
           conexion.close();
        } catch (SQLException | NullPointerException ex) {}
    }
    private int obtenerTipoError(String error){
        if(error.contains("NOT NULL")) return 1400;
        if(error.contains("not unique")) return 1;
        if(error.contains("constraint failed")) return 2290;
        if(error.contains("FOREIGN KEY constraint")) return 2292;
        else return 0;
    }
    /**
     * Inserta un coche en la base de datos.
     * @author Diego Fernández Díaz
     * @param coche coche a insertar
     * @return Cantidad de coches que se han insertado
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    @Override
    public int insertarCoche(Coche coche) throws ExcepcionGestionVehiculos {
        PreparedStatement sentenciaPreparada = null;
        String dml = null;
        int registrosAfectados = 0;
        try {
            abrirConexion();
            dml = "insert into coche(MATRICULA,MARCA,MODELO,EXTRAS,CILINDRADA,ANO,NUMERO_BASTIDOR,PRECIO_MERCADO) values(?,?,?,?,?,?,?,?)";
            sentenciaPreparada = conexion.prepareStatement(dml);
            sentenciaPreparada.setString(1, coche.getMatricula());
            sentenciaPreparada.setString(2, coche.getMarca());
            sentenciaPreparada.setString(3, coche.getModelo());
            sentenciaPreparada.setString(4, coche.getExtras());
            sentenciaPreparada.setInt(5, coche.getCilintrada());
            sentenciaPreparada.setInt(6, coche.getAño());
            sentenciaPreparada.setString(7, coche.getNumBastidor());
            sentenciaPreparada.setInt(8, coche.getPrecioMercado());
            registrosAfectados = sentenciaPreparada.executeUpdate();
            sentenciaPreparada.close();
            conexion.close();
            return registrosAfectados;
        } catch (SQLException ex) {
            ExcepcionGestionVehiculos excepcionGestionVehiculos = new ExcepcionGestionVehiculos(
                    ex.getErrorCode(),
                    ex.getMessage(), 
                    null, 
                    dml);
            switch (obtenerTipoError(ex.getMessage())) {
                case 1400:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("La matricula,marca,modelo,cilindrada,año,numero de bastidor,precio de mercado no pueden estar vacios");
                    break;
                case 1:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("La matricula y/o el numero de bastidor ya existe.");
                    break;
                case 20002:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("Los 4 primeros caracteres de la matricula tiene que ser un numero.");
                    break;  
                case 20003:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("Los 3 ultimos caracteres de la matricula tiene que ser una letra.");
                    break;
                case 12899:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("El numero de bastidor solo pueden ser 17 caracteres y/o la matricula solo puede tener 7 caracteres");
                    break;
                case 20001:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("El numero de bastidor no puede contener ninguno de estos caracteres: I, O, Q, Ñ.");
                    break;
                case 2290:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("Error por los siguientes casos: \n"
                                                                    + "La cilindrada tiene que ser menor de 10000 \n"
                                                                    + "El precio de mercado tiene que ser mayor de 100 \n"
                                                                    + "El año solo puede ser de 4 cifras");
                    break;
                default:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("Error en el sistema. Consulta con el administrador.");
                    break;
            }
            cerrarConexion(conexion, sentenciaPreparada);
            throw excepcionGestionVehiculos;
        }
    }
    /**
     * Elimina un coche de la base de datos
     * @author Diego Fernández Díaz
     * @param cocheId Identificador del coche a eliminar
     * @return Cantidad de coches eliminados
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    @Override
    public int eliminarCoche(int cocheId) throws ExcepcionGestionVehiculos {
        PreparedStatement sentenciaPreparada = null;
        String dml = null;
        int registrosAfectados = 0;
        try {
            abrirConexion();
            sentenciaPreparada = conexion.prepareStatement("pragma foreign_keys = on");
            sentenciaPreparada.executeUpdate();
            dml = "delete from coche where coche_ID=?";
            sentenciaPreparada.execute(dml);
            sentenciaPreparada.setInt(1, cocheId);
            registrosAfectados = sentenciaPreparada.executeUpdate();
            sentenciaPreparada.close();
            conexion.close();
            return registrosAfectados;
        } catch (SQLException ex) {
            ExcepcionGestionVehiculos excepcionGestionVehiculos = new ExcepcionGestionVehiculos(
                    ex.getErrorCode(), 
                    ex.getMessage(), 
                    null,
                    dml);
            switch (obtenerTipoError(ex.getMessage())) {
                case 2292:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("No se puede eliminar este coche porque tiene partes asociados");
                    break;
                default:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
                    break;
            }
            cerrarConexion(conexion, sentenciaPreparada);
            throw excepcionGestionVehiculos;
        }
    }
    /**
     * Modifica un coche de la base de datos
     * @author Diego Fernández Díaz
     * @param cocheId Identificador del coche a modificar
     * @param coche Nuevos datos del coche a modificar
     * @return Cantidad de coches modificados
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    @Override
    public int modificarCoche(int cocheId, Coche coche) throws ExcepcionGestionVehiculos {
        String dml = null;
        int registrosAfectados = 0;
        PreparedStatement sentenciaPreparada = null;
        try {
            abrirConexion();
            dml ="update coche set matricula=?, marca=?,modelo=?,"
                + "extras=?,cilindrada=?,ano=?,numero_bastidor=?,precio_mercado=? "
                + "where coche_id=" + cocheId;
            sentenciaPreparada = conexion.prepareStatement(dml);
            sentenciaPreparada.setString(1, coche.getMatricula());
            sentenciaPreparada.setString(2, coche.getMarca());
            sentenciaPreparada.setString(3, coche.getModelo());
            sentenciaPreparada.setString(4, coche.getExtras());
            sentenciaPreparada.setInt(5, coche.getCilintrada());
            sentenciaPreparada.setInt(6, coche.getAño());
            sentenciaPreparada.setString(7, coche.getNumBastidor());
            sentenciaPreparada.setInt(8, coche.getPrecioMercado());
            registrosAfectados = sentenciaPreparada.executeUpdate();
            sentenciaPreparada.close();
            conexion.close();        
            return registrosAfectados;
            } catch (SQLException ex) {
            ExcepcionGestionVehiculos excepcionGestionVehiculos = new ExcepcionGestionVehiculos(
                    ex.getErrorCode(),
                    ex.getMessage(),
                    null,
                    dml);
            switch (ex.getErrorCode()) {
                case 1400:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("La matricula,marca,modelo,cilindrada,año,numero de bastidor,precio de mercado no pueden estar vacios");
                    break;
                case 1:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("La matricula y/o el numero de bastidor ya existe.");
                    break;
                case 20002:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("Los 4 primeros caracteres de la matricula tiene que ser un numero.");
                    break;  
                case 20003:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("Los 3 ultimos caracteres de la matricula tiene que ser una letra.");
                    break;
                case 12899:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("El numero de bastidor solo pueden ser 17 caracteres y/o la matricula solo puede tener 7 caracteres");
                    break;
                case 20001:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("El numero de bastidor no puede contener ninguno de estos caracteres: I, O, Q, Ñ.");
                    break;
                case 2290:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("Error por los siguientes casos: \n"
                                                                    + "La cilindrada tiene que ser menor de 10000 \n"
                                                                    + "El precio de mercado tiene que ser mayor de 100 \n"
                                                                    + "El año solo puede ser de 4 cifras");
                    break;
                default:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("Error en el sistema. Consulta con el administrador.");
                    break;
            }
            cerrarConexion(conexion, sentenciaPreparada);
            throw excepcionGestionVehiculos;
        }
    }
    /**
     * Consulta un coche de la base de datos
     * @author Diego Fernández Díaz
     * @param cocheId Identificador del coche a consultar
     * @return Coche a consultar
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    @Override
    public Coche leerCoche(int cocheId) throws ExcepcionGestionVehiculos {
        PreparedStatement sentenciaPreparada = null;
        String dql = null;
        Coche coche=null;
        try {
            abrirConexion();
            dql = "SELECT * FROM coche WHERE coche_id=?";
            sentenciaPreparada = conexion.prepareStatement(dql);
            sentenciaPreparada.setInt(1, cocheId);
            sentenciaPreparada.executeQuery();
            ResultSet resultado = sentenciaPreparada.executeQuery(dql);
            while (resultado.next()) {
                coche=new Coche();
                coche.setCocheId(resultado.getInt("coche_id"));
                coche.setMatricula(resultado.getString("matricula"));
                coche.setMarca(resultado.getString("marca"));
                coche.setModelo(resultado.getString("modelo"));
                coche.setExtras(resultado.getString("extras"));
                coche.setCilintrada(resultado.getInt("cilindrada"));
                coche.setAño(resultado.getInt("ano"));
                coche.setNumBastidor(resultado.getString("numero_bastidor"));
                coche.setPrecioMercado(resultado.getInt("precio_mercado"));
            }
            resultado.close();
            sentenciaPreparada.close();
            conexion.close();
            return coche;
        } catch (SQLException ex) {
            ExcepcionGestionVehiculos excepcionGestionVehiculos = new ExcepcionGestionVehiculos(
                    ex.getErrorCode(), 
                    ex.getMessage(), 
                    "Error general del sistema. Consulte con el administrador.", 
                    dql);
            cerrarConexion(conexion, sentenciaPreparada);
            throw excepcionGestionVehiculos;
        }
    }
    /**
     * Consulta todos los coches de la base de datos
     * @author Diego Fernández Díaz
     * @return Lista de todos los coches
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    @Override
    public ArrayList<Coche> leerCoches() throws ExcepcionGestionVehiculos {
        String dql = "SELECT * FROM coche";
        return leerCoches(dql);
    }
    /**
     * Consulta todos los coches de la base de datos
     * @author Diego Fernández Díaz
     * @param dql
     * @return Lista de todos los coches
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    @Override
    public ArrayList<Coche> leerCoches(String dql) throws ExcepcionGestionVehiculos {
        PreparedStatement sentenciaPreparada = null;
        Coche coche = null;
        ArrayList<Coche> c = new ArrayList();
        try {
            abrirConexion();
            sentenciaPreparada = conexion.prepareStatement(dql);
            sentenciaPreparada.executeQuery();            
            ResultSet resultado = sentenciaPreparada.executeQuery(dql);
            while (resultado.next()) {
                coche = new Coche();
                coche.setCocheId(resultado.getInt("coche_id"));
                coche.setMatricula(resultado.getString("matricula"));
                coche.setMarca(resultado.getString("marca"));
                coche.setModelo(resultado.getString("modelo"));
                coche.setExtras(resultado.getString("extras"));
                coche.setCilintrada(resultado.getInt("cilindrada"));
                coche.setAño(resultado.getInt("ano"));
                coche.setNumBastidor(resultado.getString("numero_bastidor"));
                coche.setPrecioMercado(resultado.getInt("precio_mercado"));
                c.add(coche);
            }
            resultado.close();
            sentenciaPreparada.close();
            conexion.close();
            return c;
        } catch (SQLException ex) {
            ExcepcionGestionVehiculos excepcionGestionVehiculos = new ExcepcionGestionVehiculos(
                    ex.getErrorCode(), 
                    ex.getMessage(), 
                    "Error general del sistema. Consulte con el administrador.", 
                    dql);
            cerrarConexion(conexion, sentenciaPreparada);
            throw excepcionGestionVehiculos;
        }
    }
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
    @Override
    public ArrayList<Coche> leerCoches(String matricula,String marca,String modelo,
                                        String numBastidor, Integer criterioOrden, Integer orden) throws ExcepcionGestionVehiculos{
        String dql = "SELECT * FROM coche where 1=1 ";
        if (matricula !=null) dql = dql + " and matricula like " + matricula;
        if (marca != null) dql = dql + " and marca like %"+marca+"%";
        if (modelo != null) dql = dql + " and modelo like %"+modelo+"%";
        if (numBastidor != null) dql = dql + " and numero_bastidor like "+numBastidor;
        if (criterioOrden == COCHE_MARCA) {
            dql = dql + " order by marca";
            if (orden == ASCENDENTE) dql = dql + " asc";
            if (orden == DESCENDENTE) dql = dql + " desc";
        }
        if (criterioOrden == COCHE_MATRICULA) {
            dql = dql + " order by matricula";
            if (orden == ASCENDENTE) dql = dql + " asc";
            if (orden == DESCENDENTE) dql = dql + " desc";
        }
        if (criterioOrden == COCHE_MODELO) {
            dql = dql + " order by modelo";
            if (orden == ASCENDENTE) dql = dql + " asc";
            if (orden == DESCENDENTE) dql = dql + " desc";
        }
        if (criterioOrden == COCHE_NUMERO_BASTIDOR) {
            dql = dql + " order by numero_bastidor";
            if (orden == ASCENDENTE) dql = dql + " asc";
            if (orden == DESCENDENTE) dql = dql + " desc";
        }
        return leerCoches(dql);
    }
    /**
    * Inserta un parte de la base de datos
    * @author Diego Fernández Díaz
    * @param parte Nuevos datos del parte a insertar
    * @return Cantidad de partes modificados
    * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
    */
    @Override
    public int insertarParte(Parte parte) throws ExcepcionGestionVehiculos {
        PreparedStatement sentenciaPreparada = null;
        String dml = null;
        int registrosAfectados = 0;
        try {
            abrirConexion();
            sentenciaPreparada = conexion.prepareStatement("pragma foreign_keys = on");
            sentenciaPreparada.executeUpdate();
            dml = "insert into Parte(CODIGO,FECHA,COHCE_ID)values(?,?,?)";
            sentenciaPreparada.execute(dml);
            sentenciaPreparada.setString(1, parte.getCodigo());
            if (parte.getFecha()== null) {
                sentenciaPreparada.setNull(2, Types.DATE);
            } else {
                java.sql.Date fecha = new java.sql.Date(parte.getFecha().getTime());
                sentenciaPreparada.setObject(2, fecha, Types.DATE);
            }
            sentenciaPreparada.setInt(3, parte.getCocheId().getCocheId());
            registrosAfectados = sentenciaPreparada.executeUpdate();
            sentenciaPreparada.close();
            conexion.close();
            return registrosAfectados;
        } catch (SQLException ex) {
            ExcepcionGestionVehiculos excepcionGestionVehiculos = new ExcepcionGestionVehiculos(
                    ex.getErrorCode(), 
                    ex.getMessage(), 
                    null, 
                    dml);
            switch (ex.getErrorCode()) {
                case 2292:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("El coche no existe");
                    break;
                case 1400:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("El codigo,la fecha y/o el coche no pueden ser nulos");
                    break;
                case 1:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("El codigo no puede repetirse");
                    break;
                case 20004:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("Un vehiculo no puede tener mas de dos partes al mes");
                    break;
                default:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("Error en el sistema. Consulta con el administrador");
                    break;
            }
            cerrarConexion(conexion, sentenciaPreparada);
            throw excepcionGestionVehiculos;
        }
    }
    /**
     * Modifica un parte de la base de datos
     * @author Diego Fernández Díaz
     * @param parteId Identificador del parte a modificar
     * @param parte Nuevos datos del parte a modificar
     * @return Cantidad de partes modificados
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    @Override
    public int modificarParte(int parteId, Parte parte) throws ExcepcionGestionVehiculos {
        PreparedStatement sentenciaPreparada = null;
        String dml = null;
        int registrosAfectados = 0;
        try {
            abrirConexion();
            dml = "update PARTE set CODIGO=?,FECHA=? where PARTE_ID=?";
            sentenciaPreparada = conexion.prepareCall(dml);
            sentenciaPreparada.setInt(1, parteId);
            sentenciaPreparada.setString(2, parte.getCodigo());
            if (parte.getFecha()== null) {
                sentenciaPreparada.setNull(3, Types.DATE);
            } else {
                java.sql.Date fecha = new java.sql.Date(parte.getFecha().getTime());
                sentenciaPreparada.setObject(3, fecha, Types.DATE);
            }
            registrosAfectados = sentenciaPreparada.executeUpdate();
            sentenciaPreparada.close();
            conexion.close();
            return registrosAfectados;
        } catch (SQLException ex) {
            ExcepcionGestionVehiculos excepcionGestionVehiculos = new ExcepcionGestionVehiculos(
                    ex.getErrorCode(),
                    ex.getMessage(),
                    null, 
                    dml);
            switch (ex.getErrorCode()) {
                case 1400:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("El codigo y/o la fecha no pueden ser nulos");
                    break;
                case 1:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("El codigo no puede repetirse");
                    break;
                case 20004:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("Un vehiculo no puede tener mas de dos partes al mes");
                    break;
                default:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("Error en el sistema. Consulta con el administrador");
                    break;
            }
            cerrarConexion(conexion, sentenciaPreparada);
            throw excepcionGestionVehiculos;
        }
    }
    /**
     * Elimina un parte de la base de datos
     * @author Diego Fernández Díaz
     * @param parteId Identificador del parte a eliminar
     * @return Cantidad de partes eliminados
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    @Override
    public int eliminarParte(int parteId) throws ExcepcionGestionVehiculos {
        PreparedStatement sentenciaPreparada = null;
        String dml = null;
        int registrosAfectados = 0;
        try {
            abrirConexion();
            dml = "delete from parte where PARTE_ID=?";
            sentenciaPreparada =conexion.prepareStatement(dml);
            sentenciaPreparada.setInt(1, parteId);
            registrosAfectados = sentenciaPreparada.executeUpdate();
            sentenciaPreparada.close();
            conexion.close();
            return registrosAfectados;
        }catch (SQLException ex) {
            ExcepcionGestionVehiculos excepcionGestionVehiculos = new ExcepcionGestionVehiculos(
                    ex.getErrorCode(), 
                    ex.getMessage(), 
                    "Error general del sistema. Consulte con el administrador.", 
                    dml);
            cerrarConexion(conexion, sentenciaPreparada);
            throw excepcionGestionVehiculos;
        }
    }
    /**
     * Consulta un parte de la base de datos
     * @author Diego Fernández Díaz
     * @param parteId Identificador del parte a consultar
     * @return parte a consultar
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    @Override
    public Parte leerParte(int parteId) throws ExcepcionGestionVehiculos {
        PreparedStatement sentenciaPreparada = null;
        String dql = null;
        Parte parte=null;
        Coche coche = null;
        try {
            abrirConexion();
            dql = "SELECT * FROM parte WHERE parte_id=?";
            sentenciaPreparada = conexion.prepareStatement(dql);
            sentenciaPreparada.setInt(1, parteId);
            sentenciaPreparada.executeQuery();
            ResultSet resultado = sentenciaPreparada.executeQuery(dql);
            while (resultado.next()) {
                parte=new Parte();
                parte.setParteId(resultado.getInt("parte_id"));
                parte.setCodigo(resultado.getString("codigo"));
                parte.setFecha(resultado.getDate("fecha"));
                coche = new Coche();
                coche.setCocheId(resultado.getInt("coche_id"));
                parte.setCocheId(coche);
                
            }
            resultado.close();
            sentenciaPreparada.close();
            conexion.close();
            return parte;
        } catch (SQLException ex) {
            ExcepcionGestionVehiculos excepcionGestionVehiculos = new ExcepcionGestionVehiculos(
                    ex.getErrorCode(), 
                    ex.getMessage(), 
                    "Error general del sistema. Consulte con el administrador.", 
                    dql);
            cerrarConexion(conexion, sentenciaPreparada);
            throw excepcionGestionVehiculos;
        }
    }
    /**
     * Consulta todos los partes de la base de datos
     * @author Diego Fernández Díaz
     * @return Lista de todos los partes
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public ArrayList<Parte> leerPartes() throws ExcepcionGestionVehiculos {
        String dql = "SELECT * FROM parte";
        return leerParte(dql);
    }
    /**
     * Consulta todos los partes de la base de datos
     * @author Diego Fernández Díaz
     * @param dql
     * @return Lista de todos los partes
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    @Override
    public ArrayList<Parte> leerParte(String dql) throws ExcepcionGestionVehiculos {
        PreparedStatement sentenciaPreparada = null;
        Parte parte = null;
        Coche coche = null;
        ArrayList<Parte> p = new ArrayList();
        try {
            abrirConexion();
            
            sentenciaPreparada = conexion.prepareStatement(dql);
            sentenciaPreparada.executeQuery();            
            
            ResultSet resultado = sentenciaPreparada.executeQuery(dql);
            while (resultado.next()) {
                parte=new Parte();
                parte.setParteId(resultado.getInt("parte_id"));
                parte.setCodigo(resultado.getString("codigo"));
                parte.setFecha(resultado.getDate("fecha"));
                coche = new Coche();
                coche.setCocheId(resultado.getInt("coche_id"));
                parte.setCocheId(coche);
                p.add(parte);
            }
            resultado.close();
            sentenciaPreparada.close();
            conexion.close();
        } catch (SQLException ex) {
            ExcepcionGestionVehiculos excepcionGestionVehiculos = new ExcepcionGestionVehiculos(
                    ex.getErrorCode(), 
                    ex.getMessage(), 
                    "Error general del sistema. Consulte con el administrador.", 
                    dql);
            cerrarConexion(conexion, sentenciaPreparada);
            throw excepcionGestionVehiculos;
        }
        return p;
    }
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
    
    public ArrayList<Parte> leerPartes(String codigo,Date fecha,Integer cocheId,Integer criterioOrden, Integer orden) throws ExcepcionGestionVehiculos{
        String dql = "SELECT * FROM parte where 1=1 ";
        if (codigo !=null) dql = dql + " and codigo like " + codigo;
        if (fecha != null) dql = dql + " and date_format(hi.fecha,'%Y-%m-%d') = '"+ formatearFecha(fecha)+"'";
        if (cocheId != null) dql = dql + " and coche_id like %"+cocheId+"%";
        if (criterioOrden == PARTE_CODIGO) {
            dql = dql + " order by marca";
            if (orden == ASCENDENTE) dql = dql + " asc";
            if (orden == DESCENDENTE) dql = dql + " desc";
        }
        if (criterioOrden == PARTE_FECHA) {
            dql = dql + " order by matricula";
            if (orden == ASCENDENTE) dql = dql + " asc";
            if (orden == DESCENDENTE) dql = dql + " desc";
        }
        if (criterioOrden == PARTE_COCHE) {
            dql = dql + " order by modelo";
            if (orden == ASCENDENTE) dql = dql + " asc";
            if (orden == DESCENDENTE) dql = dql + " desc";
        }
        return leerParte(dql);
    }
    /**
     * Formatea una fecha a cadena
     * @author Diego Fernández Díaz
     * @param fecha fecha a formatear
     * @return cadena con la fecha en formato YYYY-MM-DD
     */
    private String formatearFecha(Date fecha)
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(fecha);
        return gc.get(Calendar.YEAR)+ "-"+(gc.get(Calendar.MONTH)+1)+"-"+gc.get(Calendar.DAY_OF_MONTH);
    }
    
}

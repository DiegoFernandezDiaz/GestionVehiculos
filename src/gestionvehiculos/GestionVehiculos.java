
package gestionvehiculos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;



/**
 *
 * @author Diego Fernández Díaz
 */
public class GestionVehiculos {
    
    Connection conexion;

    /**
     * Constructor vacío
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public GestionVehiculos() throws ExcepcionGestionVehiculos {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conexion = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "HR", "kk");
        } catch (ClassNotFoundException ex) {
            ExcepcionGestionVehiculos excepcionGestionVehiculos = new ExcepcionGestionVehiculos(-1, ex.getMessage(), "Error general del sistema. Consulte con el administrador.", null);
            throw excepcionGestionVehiculos;
        } catch (SQLException ex) {
            ExcepcionGestionVehiculos excepcionGestionVehiculos = new ExcepcionGestionVehiculos(ex.getErrorCode(), ex.getMessage(), "Error general del sistema. Consulte con el administrador.", null);
            throw excepcionGestionVehiculos;
        }
    }

    /**
     * Cierra de forma ordenada un objeto Statement y la conexión de la base de 
     * datos
     * @author Ignacio Fontecha Hernández
     * @param conexion Conexion a cerrar
     */
    private void cerrarConexion(Connection conexion, Statement sentencia) {
        try {
           sentencia.close();
           conexion.close();
        } catch (SQLException | NullPointerException ex) {}
    }
    
    /**
     * Cierra de forma ordenada un objeto Statement y la conexión de la base de 
     * datos
     * @author Ignacio Fontecha Hernández
     * @param conexion Conexion a cerrar
     */
    private void cerrarConexion(Connection conexion, PreparedStatement sentenciaPreparada) {
        try {
           sentenciaPreparada.close();
           conexion.close();
        } catch (SQLException | NullPointerException ex) {}
    }
    
    /**
     * Cierra de forma ordenada un objeto Statement y la conexión de la base de 
     * datos
     * @author Ignacio Fontecha Hernández
     * @param conexion Conexion a cerrar
     */
    private void cerrarConexion(Connection conexion, CallableStatement sentenciaLlamable) {
        try {
           sentenciaLlamable.close();
           conexion.close();
        } catch (SQLException | NullPointerException ex) {}
       
    }
    
    /**
     * Inserta una región en la base de datos.
     * @author David Fernandez Garcia
     * @param coche Región a insertar
     * @return Cantidad de regiones que se han insertado
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public int insertarCoche(Coche coche) throws ExcepcionGestionVehiculos {
        PreparedStatement sentenciaPreparada = null;
        String dml = null;
        int registrosAfectados = 0;
        try {
            dml = "insert into coche(id_coche,matricula,marca,modelo,extras,año,num_bastidor,precio_venta) values(?,?,?,?,?,?,?,?)";
            sentenciaPreparada = conexion.prepareStatement(dml);
            sentenciaPreparada.setInt(1, coche.getCocheId());
            sentenciaPreparada.setString(2, coche.getMatricula());
            sentenciaPreparada.setString(3, coche.getMarca());
            sentenciaPreparada.setString(4, coche.getModelo());
            sentenciaPreparada.setString(5, coche.getMatricula());
            sentenciaPreparada.setString(6, coche.getMatricula());
            sentenciaPreparada.setString(7, coche.getMatricula());
            sentenciaPreparada.setString(8, coche.getMatricula());
            registrosAfectados = sentenciaPreparada.executeUpdate();
            sentenciaPreparada.close();
            conexion.close();
        } catch (SQLException ex) {
            ExcepcionHR excepcionHR = new ExcepcionHR(ex.getErrorCode(), ex.getMessage(), null, dml);
            switch (ex.getErrorCode()) {
                case 1400:
                    excepcionHR.setMensajeErrorUsuario("El identificador del pais no puede ser nulo");
                    break;
                case 1:
                    excepcionHR.setMensajeErrorUsuario("El identificador del pais no puede repetirse.");
                    break;
                default:
                    excepcionHR.setMensajeErrorUsuario("Error en el sistema. Consulta con el administrador.");
                    break;
            }
            cerrarConexion(conexion, sentenciaPreparada);
            throw excepcionHR;
        }
        return registrosAfectados;
    }

    /**
     * Elimina una region de la base de datos
     * @author Ricardo Pérez Barreda
     * @param regionId Identificador de la región a eliminar
     * @return Cantidad de regiones eliminadas
     * @throws ExcepcionHR si se produce cualquier excepcion
     */
    public int borrarRegion(int regionId) throws ExcepcionHR {
        PreparedStatement sentenciaPreparada = null;
        String dml = null;
        int registrosAfectados = 0;
        try {
            dml = "delete from REGIONS where REGION_ID=?";
            sentenciaPreparada = conexion.prepareStatement(dml);
            sentenciaPreparada.setInt(1, regionId);
            registrosAfectados = sentenciaPreparada.executeUpdate();
            sentenciaPreparada.close();
            conexion.close();
        } catch (SQLException ex) {
            ExcepcionHR excepcionHR = new ExcepcionHR(ex.getErrorCode(), ex.getMessage(), "Error general del sistema. Consulte con el administrador.", null);
            switch (ex.getErrorCode()) {
                case 2292:
                    excepcionHR.setMensajeErrorUsuario("No se puede eliminar esta region porque tiene paises asociados");
                    break;
                default:
                    excepcionHR.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
                    break;
            }
            cerrarConexion(conexion, sentenciaPreparada);
            throw excepcionHR;
        }
        return registrosAfectados;
    }

    /**
     * Modifica una región de la base de datos
     * @author Jonathan León Lorenzo
     * @param regionId Identificador de la región a modificar
     * @param region Nuevos datos de la región a modificar
     * @return Cantidad de regiones modificadas
     * @throws ExcepcionHR si se produce cualquier excepcion
     */
    public int modificarRegion(int regionId, Region region) throws ExcepcionHR {
        Statement sentencia = null;
        String dml = null;
        int registrosAfectados = 0;
        try {
            sentencia = conexion.createStatement();
            dml = "update regions set REGION_ID=" + region.getRegionId() + ", REGION_NAME='" + region.getRegionName() + "' where REGION_ID=" + regionId;
            registrosAfectados = sentencia.executeUpdate(dml);
            sentencia.close();
            conexion.close();
        } catch (SQLException ex) {
            ExcepcionHR excepcionHR = new ExcepcionHR(ex.getErrorCode(), ex.getMessage(), "Error general del sistema. Consulte con el administrador.", null);
            switch (ex.getErrorCode()) {
                case 2292:
                    excepcionHR.setMensajeErrorUsuario("No se puede modificar el codigo del continente mientras tenga paises asociados.");
                    break;
                case 1:
                    excepcionHR.setMensajeErrorUsuario("No se puede modificar el código del continente porque el nuevo código está siendo utilizado por otro continente.");
                    break;
                default:
                    excepcionHR.setMensajeErrorUsuario("Error en el sistema. Consulta con el administrador" + ex);
                    break;
            }
            cerrarConexion(conexion, sentencia);
            throw excepcionHR;
        }
        return registrosAfectados;
    }

    /**
     * Consulta una región de la base de datos
     * @author Jonathan Leon-Byron Morales
     * @param regionId Identificador de la región a consultar
     * @return Región a consultar
     * @throws ExcepcionHR si se produce cualquier excepcion
     */
    public Region leerRegion(int regionId) throws ExcepcionHR {
        PreparedStatement sentenciaPreparada = null;
        String dql = null;
        Region r = null;
        Region region=null;
        try {
            dql = "SELECT * FROM REGIONS WHERE REGION_ID=?";
            sentenciaPreparada = conexion.prepareStatement(dql);
            sentenciaPreparada.setInt(1, regionId);
            sentenciaPreparada.executeQuery();
            
            ResultSet resultado = sentenciaPreparada.executeQuery(dql);
            while (resultado.next()) {
                region=new Region();
                region.setRegionId(resultado.getInt("REGION_ID"));
                region.setRegionName(resultado.getString("REGION_NAME"));
            }
            resultado.close();
            sentenciaPreparada.close();
            conexion.close();
            
        } catch (SQLException ex) {
            ExcepcionHR excepcionHR = new ExcepcionHR(
                    ex.getErrorCode(), 
                    ex.getMessage(), 
                    "Error general del sistema. Consulte con el administrador.", 
                    dql);
            cerrarConexion(conexion, sentenciaPreparada);
            throw excepcionHR;
        }
        return region;
    }

    /**
     * Consulta todas las regiones de la base de datos
     * @author Jonathan León-Byron Morales
     * @return Lista de todas las regiones
     * @throws ExcepcionHR si se produce cualquier excepcion
     */
    public ArrayList<Region> leerRegions() throws ExcepcionHR {
        PreparedStatement sentenciaPreparada = null;
        String dql = null;
        Region region = null;
        ArrayList<Region> a = new ArrayList();
        try {
            dql = "SELECT * FROM REGIONS";
            sentenciaPreparada = conexion.prepareStatement(dql);
            sentenciaPreparada.executeQuery();            
            
            ResultSet resultado = sentenciaPreparada.executeQuery(dql);
            while (resultado.next()) {
                region = new Region();
                region.setRegionId(resultado.getInt("REGION_ID"));
                region.setRegionName(resultado.getString("REGION_NAME"));
                a.add(region);
            }
            resultado.close();
            sentenciaPreparada.close();
            conexion.close();
        } catch (SQLException ex) {
            ExcepcionHR excepcionHR = new ExcepcionHR(
                    ex.getErrorCode(), 
                    ex.getMessage(), 
                    "Error general del sistema. Consulte con el administrador.", 
                    dql);
            cerrarConexion(conexion, sentenciaPreparada);
            throw excepcionHR;
        }
        return a;
    }
      
    
}

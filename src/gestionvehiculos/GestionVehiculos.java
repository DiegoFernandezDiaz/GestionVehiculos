
package gestionvehiculos;

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
 * @author Diego Fernández Díaz
 */
public class GestionVehiculos {

    private Connection conexion;
    private String cadenaConexion = "jdbc:oracle:thin:@10.0.2.15:1521:xe\", \"HR\", \"kk";
    

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
     * Constructor vacío
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public GestionVehiculos() throws ExcepcionGestionVehiculos {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
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
     * Abre la conexión con la base de datos 
     * @author Diego Fernández Díaz
     */
    private void abrirConexion() throws ExcepcionGestionVehiculos {
        try {
            conexion = DriverManager.getConnection(cadenaConexion);
        } catch (SQLException ex) {
            ExcepcionGestionVehiculos e = new ExcepcionGestionVehiculos(
                    ex.getErrorCode(),
                    ex.getMessage(),
                    null,
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
    
    /**
     * Cierra de forma ordenada un objeto Statement y la conexión de la base de 
     * datos
     * @author Diego Fernández Díaz
     * @param conexion Conexion a cerrar
     */
    private void cerrarConexion(Connection conexion, CallableStatement sentenciaLlamable) {
        try {
           sentenciaLlamable.close();
           conexion.close();
        } catch (SQLException | NullPointerException ex) {}
       
    }
    
    /**
     * Inserta un coche en la base de datos.
     * @author Diego Fernández Díaz
     * @param coche coche a insertar
     * @return Cantidad de coches que se han insertado
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public int insertarCoche(Coche coche) throws ExcepcionGestionVehiculos {
        PreparedStatement sentenciaPreparada = null;
        String dml = null;
        int registrosAfectados = 0;
        try {
            abrirConexion();
            dml = "insert into coche(coche_id,matricula,marca,modelo,extras,cilindrada,año,numero_bastidor,precio_mercado) values(PARTE_SEQ.NextValue,?,?,?,?,?,?,?,?)";
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
        } catch (SQLException ex) {
            ExcepcionGestionVehiculos excepcionGestionVehiculos = new ExcepcionGestionVehiculos(ex.getErrorCode(), ex.getMessage(), null, dml);
            switch (ex.getErrorCode()) {
                case 1400:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("La matricula,marca,modelo,cilindrada,año,numero de bastidor,precio de mercado no pueden estar vacios");
                    break;
                case 1:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("La matricula y/o el numero de bastidor ya existe.");
                    break;
                default:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("Error en el sistema. Consulta con el administrador.");
                    break;
            }
            cerrarConexion(conexion, sentenciaPreparada);
            throw excepcionGestionVehiculos;
        }
        return registrosAfectados;
    }

    /**
     * Elimina un coche de la base de datos
     * @author Diego Fernández Díaz
     * @param cocheId Identificador del coche a eliminar
     * @return Cantidad de coches eliminados
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public int borrarCoche(int cocheId) throws ExcepcionGestionVehiculos {
        PreparedStatement sentenciaPreparada = null;
        String dml = null;
        int registrosAfectados = 0;
        try {
            abrirConexion();
            dml = "delete from coche where coche_ID=?";
            sentenciaPreparada = conexion.prepareStatement(dml);
            sentenciaPreparada.setInt(1, cocheId);
            registrosAfectados = sentenciaPreparada.executeUpdate();
            sentenciaPreparada.close();
            conexion.close();
        } catch (SQLException ex) {
            ExcepcionGestionVehiculos excepcionGestionVehiculos = new ExcepcionGestionVehiculos(ex.getErrorCode(), ex.getMessage(), "Error general del sistema. Consulte con el administrador.", null);
            switch (ex.getErrorCode()) {
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
        return registrosAfectados;
    }

    /**
     * Modifica un coche de la base de datos
     * @author Diego Fernández Díaz
     * @param cocheId Identificador del coche a modificar
     * @param coche Nuevos datos del coche a modificar
     * @return Cantidad de coches modificados
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public int modificarCoche(int cocheId, Coche coche) throws ExcepcionGestionVehiculos {
        String dml = null;
        int registrosAfectados = 0;
        PreparedStatement sentenciaPreparada = null;
        try {
            abrirConexion();
            dml ="update coche set matricula=?, marca=?,modelo=?,"
                + "extras=?,cilindrada=?,año=?,numero_bastidor=?,precio_mercado=? "
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
            } catch (SQLException ex) {
            ExcepcionGestionVehiculos excepcionGestionVehiculos = new ExcepcionGestionVehiculos(ex.getErrorCode(), ex.getMessage(), "Error general del sistema. Consulte con el administrador.", null);
            switch (ex.getErrorCode()) {
                case 2292:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("No se puede modificar el codigo del continente mientras tenga paises asociados.");
                    break;
                case 1:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("No se puede modificar el código del continente porque el nuevo código está siendo utilizado por otro continente.");
                    break;
                default:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("Error en el sistema. Consulta con el administrador" + ex);
                    break;
            }
            cerrarConexion(conexion, sentenciaPreparada);
            throw excepcionGestionVehiculos;
        }
        return registrosAfectados;
    }

    /**
     * Consulta un coche de la base de datos
     * @author Diego Fernández Díaz
     * @param cocheId Identificador del coche a consultar
     * @return Coche a consultar
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
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
                coche.setAño(resultado.getInt("año"));
                coche.setNumBastidor(resultado.getString("numero_bastidor"));
                coche.setPrecioMercado(resultado.getInt("precio_mercado"));
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
        return coche;
    }

    /**
     * Consulta todos los coches de la base de datos
     * @author Diego Fernández Díaz
     * @return Lista de todos los coches
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
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
    private ArrayList<Coche> leerCoches(String dql) throws ExcepcionGestionVehiculos {
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
                coche.setAño(resultado.getInt("año"));
                coche.setNumBastidor(resultado.getString("numero_bastidor"));
                coche.setPrecioMercado(resultado.getInt("precio_mercado"));
                c.add(coche);
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
        return c;
    }
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
     * Elimina un parte de la base de datos
     * @author Diego Fernández Díaz
     * @param parteId Identificador del parte a eliminar
     * @return Cantidad de partes eliminados
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public int EliminarParte(int parteId) throws ExcepcionGestionVehiculos {
        CallableStatement sentenciaLlamable = null;
        String llamada = null;
        int registrosAfectados = 0;
        try {
            abrirConexion();
            llamada = "call ELIMINAR_PARTE(?)";
            sentenciaLlamable = conexion.prepareCall(llamada);
            sentenciaLlamable.setInt(1, parteId);
            registrosAfectados = sentenciaLlamable.executeUpdate();
            sentenciaLlamable.close();
            conexion.close();
        } catch (SQLException ex) {
            ExcepcionGestionVehiculos excepcionGestionVehiculos = new ExcepcionGestionVehiculos(ex.getErrorCode(), ex.getMessage(), "Error general del sistema. Consulte con el administrador.", null);
            switch (ex.getErrorCode()) {
                case 2292:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("No se puede borrar porque tiene localidades asociadas");
                    break;
                default:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
                    break;
            }
            cerrarConexion(conexion, sentenciaLlamable);
            throw excepcionGestionVehiculos;
        }
        return registrosAfectados;
    }

    /**
     * Modifica un parte de la base de datos
     * @author Diego Fernández Díaz
     * @param parteId Identificador del parte a modificar
     * @param parte Nuevos datos del parte a modificar
     * @return Cantidad de partes modificados
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public int modificarParte(int parteId, Parte parte) throws ExcepcionGestionVehiculos {
        CallableStatement sentenciaLlamable = null;
        String llamada = null;
        int registrosAfectados = 0;
        try {
            abrirConexion();
            llamada = "call MODIFICAR_PARTE(?,?,?,?)";
            sentenciaLlamable = conexion.prepareCall(llamada);
            sentenciaLlamable.setInt(1, parteId);
            sentenciaLlamable.setString(2, parte.getCodigo());
            if (parte.getFecha()== null) {
                sentenciaLlamable.setNull(3, Types.DATE);
            } else {
                java.sql.Date fecha = new java.sql.Date(parte.getFecha().getTime());
                sentenciaLlamable.setObject(3, fecha, Types.DATE);
            }
            sentenciaLlamable.setInt(4, parte.getCocheId().getCocheId());
            registrosAfectados = sentenciaLlamable.executeUpdate();
            sentenciaLlamable.close();
            conexion.close();
        } catch (SQLException ex) {
            ExcepcionGestionVehiculos excepcionGestionVehiculos = new ExcepcionGestionVehiculos(ex.getErrorCode(), ex.getMessage(), null, llamada);
            switch (ex.getErrorCode()) {
                case 2292:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("No se puede modificar el identificador, tiene localidades asociadas.");
                    break;
                case 2291:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("La region no existe");
                    break;
                case 1407:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("El identificador del pais no puede ser nulo");
                    break;
                case 1:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("El identificador de pais no puede repetirse");
                    break;
                default:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("Error en el sistema. Consulta con el administrador");
                    break;
            }
            cerrarConexion(conexion, sentenciaLlamable);
            throw excepcionGestionVehiculos;
        }
        return registrosAfectados;
    }
      /**
     * Modifica un parte de la base de datos
     * @author Diego Fernández Díaz
     * @param parte Nuevos datos del parte a modificar
     * @return Cantidad de partes modificados
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
    public int InsertarParte(Parte parte) throws ExcepcionGestionVehiculos {
        CallableStatement sentenciaLlamable = null;
        String llamada = null;
        int registrosAfectados = 0;
        try {
            abrirConexion();
            llamada = "call INSERTAR_PARTE(?,?,?)";
            sentenciaLlamable = conexion.prepareCall(llamada);
            sentenciaLlamable.setString(1, parte.getCodigo());
            if (parte.getFecha()== null) {
                sentenciaLlamable.setNull(2, Types.DATE);
            } else {
                java.sql.Date fecha = new java.sql.Date(parte.getFecha().getTime());
                sentenciaLlamable.setObject(2, fecha, Types.DATE);
            }
            sentenciaLlamable.setInt(3, parte.getCocheId().getCocheId());
            registrosAfectados = sentenciaLlamable.executeUpdate();
            sentenciaLlamable.close();
            conexion.close();
        } catch (SQLException ex) {
            ExcepcionGestionVehiculos excepcionGestionVehiculos = new ExcepcionGestionVehiculos(ex.getErrorCode(), ex.getMessage(), null, llamada);
            switch (ex.getErrorCode()) {
                case 2292:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("No se puede modificar el identificador, tiene localidades asociadas.");
                    break;
                case 2291:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("La region no existe");
                    break;
                case 1407:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("El identificador del pais no puede ser nulo");
                    break;
                case 1:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("El identificador de pais no puede repetirse");
                    break;
                default:
                    excepcionGestionVehiculos.setMensajeErrorUsuario("Error en el sistema. Consulta con el administrador");
                    break;
            }
            cerrarConexion(conexion, sentenciaLlamable);
            throw excepcionGestionVehiculos;
        }
        return registrosAfectados;
    }
    /**
     * Consulta un parte de la base de datos
     * @author Diego Fernández Díaz
     * @param parteId Identificador del parte a consultar
     * @return parte a consultar
     * @throws ExcepcionGestionVehiculos si se produce cualquier excepcion
     */
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
            
        } catch (SQLException ex) {
            ExcepcionGestionVehiculos excepcionGestionVehiculos = new ExcepcionGestionVehiculos(
                    ex.getErrorCode(), 
                    ex.getMessage(), 
                    "Error general del sistema. Consulte con el administrador.", 
                    dql);
            cerrarConexion(conexion, sentenciaPreparada);
            throw excepcionGestionVehiculos;
        }
        return parte;
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
    private ArrayList<Parte> leerParte(String dql) throws ExcepcionGestionVehiculos {
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
    }public ArrayList<Parte> leerPartes(String codigo,Date fecha,Integer cocheId,Integer criterioOrden, Integer orden) throws ExcepcionGestionVehiculos{
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
    
    private String formatearFecha(Date fecha)
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(fecha);
        return gc.get(Calendar.YEAR)+ "-"+(gc.get(Calendar.MONTH)+1)+"-"+gc.get(Calendar.DAY_OF_MONTH);
    }
    
}

package com.cice.libreriadb;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

/**
 * Clase encargada de generar el acceso y uso de la base de datps
 */

public class libreriadb{

    private final String DRIVER;
    private final String HOST;
    private final String PUERTO;
    private final String USER;
    private final String PASS;
    private final String DATABASE;

    private Connection connection;
    private Statement statement;

    public libreriadb(){
        this.DRIVER = "com.mysql.jdbc.Driver";
        this.HOST = "localhost";
        this.PUERTO = "8889";
        this.USER = "root";
        this.PASS = "root";
        this.DATABASE = "diego";
    }

    public libreriadb(String DRIVER, String HOST, String PUERTO, String USER, String PASS, String DATABASE) {
        this.DRIVER = DRIVER;
        this.HOST = HOST;
        this.PUERTO = PUERTO;
        this.USER = USER;
        this.PASS = PASS;
        this.DATABASE = DATABASE;
    }

    private String generarUrl(){
        return "jdbc:mysql://"+HOST+":"+PUERTO+"/"+DATABASE;
    }

    /**
     * Metodo que se utiliza para conectar contra una base de datos, ya se por defecto
     * o inicializada según los parámetros del constructor
     * @return estado de conexión (true en caso afirmativo)
     */
    private boolean conectarBaseDatos(){
        boolean esConectado = false;
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(generarUrl(),USER, PASS);
            if(connection != null){
                esConectado = true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return esConectado;
    }

    /**
     * Método que usaremos para desconectarnosde la base de datos y asi liberar recursos
     * @return estado de conexion (true si se desconecta)
     */
    private boolean desconectarBaseDatos(){
        boolean esDesconectado = false;
        try {
            connection.close();
            esDesconectado = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return esDesconectado;
    }

    /**
     * Ejecuta un select utilizando CachedRowSet
     * @param sql
     * @return devuelve un cachedRowSet
     */
    public CachedRowSet ejecutarSelect(String sql){

        CachedRowSet resultado = null;
        ResultSet resultSet = null;
        System.out.println(conectarBaseDatos());

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            resultado = RowSetProvider.newFactory().createCachedRowSet();
            resultado.populate(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        desconectarBaseDatos();
        return resultado;
    }

    public void ejecutarUpdate(String sql){

        System.out.println(conectarBaseDatos());

        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        desconectarBaseDatos();

    }
}


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paquete;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 *
 * @author ayele
 */
public class Conexion {
    
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/abaLaGloria?useSSL=false&serverTimezone=UTC";
    private static final String usuario = "root";
    private static final String contraseña = "Nami3003";
    private Connection conexion;

    
    public Conexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, usuario, contraseña);
            System.out.println("Conexión exitosa a la base de datos");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver de MySQL.");
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }
    
   public Connection getConexion() {
        return conexion;
    }
    
    
    // Método genérico para insertar
    public boolean insertar(String tabla, String columnas, String valores, Object... parametros) {
        String sql = "INSERT INTO " + tabla + " (" + columnas + ") VALUES (" + valores + ")";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            for (int i = 0; i < parametros.length; i++) {
                stmt.setObject(i + 1, parametros[i]);
            }
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar en " + tabla + ": " + e.getMessage());
            return false;
        }
    }

    // Método genérico para actualizar
    public boolean actualizar(String tabla, String setClause, String whereClause, Object... parametros) {
        String sql = "UPDATE " + tabla + " SET " + setClause + " WHERE " + whereClause;
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            for (int i = 0; i < parametros.length; i++) {
                stmt.setObject(i + 1, parametros[i]);
            }
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al actualizar en " + tabla + ": " + e.getMessage());
            return false;
        }
    }

    // Método genérico para eliminar
    public boolean eliminar(String tabla, String whereClause, Object... parametros) {
        String sql = "DELETE FROM " + tabla + " WHERE " + whereClause;
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            for (int i = 0; i < parametros.length; i++) {
                stmt.setObject(i + 1, parametros[i]);
            }
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al eliminar de " + tabla + ": " + e.getMessage());
            return false;
        }
    }

    // Método genérico para consultar todos los registros de una tabla
    public ResultSet consultarTodo(String tabla) {
        String sql = "SELECT * FROM " + tabla;
        try {
            PreparedStatement stmt = conexion.prepareStatement(sql);
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error al consultar " + tabla + ": " + e.getMessage());
            return null;
        }
    }

    // Método genérico para buscar por ID
    public ResultSet buscarPorId(String tabla, String campoID, Object id) {
        String sql = "SELECT * FROM " + tabla + " WHERE " + campoID + " = ?";
        try {
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setObject(1, id);
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error al buscar en " + tabla + ": " + e.getMessage());
            return null;
        }
    }
// Método genérico para ejecutar consulta personalizada con parámetros
    public ResultSet ejecutarConsulta(String sql, Object... parametros) {
        try {
            PreparedStatement stmt = conexion.prepareStatement(sql);
            for (int i = 0; i < parametros.length; i++) {
                stmt.setObject(i + 1, parametros[i]);
            }
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
            return null;
        }
    }
    
   //Método génerico para consultar múltiples condiciones
    public ResultSet buscarPorCampos(String tabla, String[] campos, Object[] valores) {
    if (campos.length != valores.length) throw new IllegalArgumentException("Los arrays deben tener la misma longitud.");

    StringBuilder sql = new StringBuilder("SELECT * FROM " + tabla + " WHERE ");
    for (int i = 0; i < campos.length; i++) {
        sql.append(campos[i]).append(" = ?");
        if (i < campos.length - 1) {
            sql.append(" AND ");
        }
    }

    try {
        PreparedStatement stmt = conexion.prepareStatement(sql.toString());
        for (int i = 0; i < valores.length; i++) {
            stmt.setObject(i + 1, valores[i]);
        }
        return stmt.executeQuery();
    } catch (SQLException e) {
        System.err.println("Error en búsqueda múltiple: " + e.getMessage());
        return null;
    }
}

    // Actualiza un registro por campo
    public boolean actualizarPorCampo(String tabla, Map<String, Object> campos, String campoClave, Object valorClave) {
    StringBuilder sql = new StringBuilder("UPDATE " + tabla + " SET ");
    int count = 0;
    for (String campo : campos.keySet()) {
        sql.append(campo).append(" = ?");
        if (++count < campos.size()) sql.append(", ");
    }
    sql.append(" WHERE ").append(campoClave).append(" = ?");

    try {
        PreparedStatement stmt = conexion.prepareStatement(sql.toString());
        int index = 1;
        for (Object val : campos.values()) {
            stmt.setObject(index++, val);
        }
        stmt.setObject(index, valorClave);
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error al actualizar en " + tabla + ": " + e.getMessage());
        return false;
    }
}
    public ResultSet showColumnsLike(String tabla, String filtroLike) {
    String sql = "SHOW COLUMNS FROM " + tabla + " LIKE ?";
    try {
        PreparedStatement stmt = conexion.prepareStatement(sql);
        stmt.setString(1, filtroLike);
        return stmt.executeQuery();
    } catch (SQLException e) {
        System.err.println("Error en showColumnsLike: " + e.getMessage());
        return null;
    }
}

    public ResultSet describeTable(String tabla) {
    String sql = "DESCRIBE " + tabla;
    try {
        PreparedStatement stmt = conexion.prepareStatement(sql);
        return stmt.executeQuery();
    } catch (SQLException e) {
        System.err.println("Error en describeTable: " + e.getMessage());
        return null;
    }
}

    public ResultSet showWithFilter(String filtro) {
    String sql = "SHOW COLUMNS FROM usuario LIKE ?";
    try {
        PreparedStatement stmt = conexion.prepareStatement(sql);
        stmt.setString(1, filtro);
        return stmt.executeQuery();
    } catch (SQLException e) {
        System.err.println("Error en showWithFilter: " + e.getMessage());
        return null;
    }
}

    
}




/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author ayele
 */
public class Sesion {
    
    private static int idUsuario;
    private static int rol;
    public static String nuevaContraseña = null;
    public static String nombreUsuario;
    public static String correo;

    public static int getIdUsuario() {
        return idUsuario;
    }

    public static void setIdUsuario(int id) {
        idUsuario = id;
    }

    public static int getRol() {
        return rol;
    }

    public static void setRol(int rolUsuario) {
        rol = rolUsuario;
    }
    
    public static String getNombreUsuario() {
        return nombreUsuario;
    }

    public static void setNombreUsuario(String nombreUsuario) {
        Sesion.nombreUsuario = nombreUsuario;
    }
    
    public static String getCorreo() {
        return correo;
    }

    public static void setCorreo(String correo) {
        Sesion.correo = correo;
    }
}


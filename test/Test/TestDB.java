/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Test;

import Util.ConexionSingleton;
import java.sql.*;


public class TestDB {

    
    public static void main(String[] args) {
        TestDB t = new TestDB();
        t.testConexion();
    }
    public void testConexion() {
        ConexionSingleton conn = new ConexionSingleton();
        try {
            Connection connection = conn.getConnection();
            if (connection != null && !connection.isClosed()) {
                System.out.println("Conexion satisfactoria!!!");
            } else {
                System.out.println("No se puede establecer conexion");
            }
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    
}

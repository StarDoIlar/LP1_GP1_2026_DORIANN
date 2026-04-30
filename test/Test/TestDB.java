
package Test;

import java.sql.*;
import util.ConexionSingleton;

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
                System.out.println("Conexion satisfactoria!!");
            } else {                             
                   System.out.println("No hay conexion");
            }
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            e.printStackTrace();
        }
    }                           
}
   
    


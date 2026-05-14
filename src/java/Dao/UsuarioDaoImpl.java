
package Dao;

import Interface.IUsuario;
import models.Usuario;
import java.sql.*;
import models.Persona;
import models.rol;
import util.ConexionSingleton;

public class UsuarioDaoImpl implements IUsuario{
    private Connection cn;

    @Override
    public Usuario validate(String user, String password) {
        Usuario u=null;
        Persona p=null;
        PreparedStatement st;
        ResultSet rs;
        String query = null;
        try {
            u = new Usuario();
            p = new Persona();
            String hashedPassword = u.HashPassword(password);
            query = "SELECT u.id_usuario, u.usuario, u.rol, p.id_persona, p.nombre"
                    + "FROM usuarios u, persona p"
                    + "WHERE p.id_persona = u.id_persona"
                    + "AND u.usuario = ?"
                    + "AND u.password = ?";
            cn = ConexionSingleton.getConnection();
            st = cn.prepareStatement(query);
            st.setString(1, user);
            st.setString(2, hashedPassword);
            rs = st.executeQuery();
            while (rs.next()) {                
                u = new Usuario();
                u.setId_usuario(rs.getInt("id_usuario"));
                u.setUsuario(rs.getString("usuario"));
                u.setRol(rol.valueOf(rs.getString("rol").toUpperCase()));
                p.setId_persona(rs.getInt("id_persona"));
                p.setNombre(rs.getString("nombre"));
                u.setPersona(p);
            }
            
        } catch (Exception e) {
            System.out.println("Error al agregar user: "+e.getMessage());
            try {
                cn.rollback();
            } catch (Exception ex) {
                System.out.println("Error de rollback: "+ex.getMessage());
            }
        } finally {
            if (cn!=null) {
                try {
                    
                } catch (Exception e) {
                }
            }
        }
        return u;
    }   
    
    
}

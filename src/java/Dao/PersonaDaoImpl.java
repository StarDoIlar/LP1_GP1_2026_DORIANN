
package Dao;

import Interface.IPersona;
import java.sql.*;
import java.util.List;
import models.Persona;
import models.Usuario;
import models.rol;
import util.ConexionSingleton;

public class PersonaDaoImpl implements IPersona{
    private Connection cn;

    @Override
    public List<Persona> lista() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int insertar(Persona p, Usuario u) {
        PreparedStatement st;
        String query = null;
        ResultSet rs;
        int id_persona = 0;
        int r = 0;
        try {
            query = "INSERT INTO persona(nombre, email, telefono, direccion)" +
                    "values(?,?,?,?)";
            cn = ConexionSingleton.getConnection();
            st = cn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            st.setString(1, p.getNombre());
            st.setString(2, p.getEmail());
            st.setString(3, p.getTelefono());
            st.setString(4, p.getDireccion());
            r = st.executeUpdate();
            if (r != 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    id_persona = rs.getInt(1);
                    System.out.println("id_recuperado" +id_persona);
                }
                if (id_persona>0) {
                    u.setRol(rol.CLIENTE);
                    String hashedPassword = u.HashPassword(u.getPassword());
                    query = " INSERT INTO usuarios(usuario, password, rol, id_persona)"
                            + "VALUES(?,?,?,?)";
                    st = cn.prepareStatement(query);
                    st.setString(1, p.getEmail());
                    st.setString(2, hashedPassword);
                    st.setString(3, u.getRol().name());
                    st.setInt(4, id_persona);
                    r = st.executeUpdate();
                } else {
                    System.out.println("Error a l agregar persona");
                }
            }
        } catch (Exception e) {
            System.out.println("Error al agregar: "+e.getMessage());
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
        return r;
    }

    @Override
    public boolean update(Persona p) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Persona SearchById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}

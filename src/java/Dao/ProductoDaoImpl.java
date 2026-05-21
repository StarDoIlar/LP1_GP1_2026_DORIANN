
package Dao;

import Interface.IProducto;
import java.util.List;
import models.Producto;
import java.sql.*;
import java.util.ArrayList;
import util.ConexionSingleton;

public class ProductoDaoImpl implements IProducto{
    private Connection cn;
    static PreparedStatement st;
    static ResultSet rs;
    static String query = null;

    @Override
    public List<Producto> lista() {
        List<Producto> lista = null;
        Producto pr;
        
        try {
            query = "SELECT id_producto, nombre, descripcion, precio, stock "
                    + "FROM productos";
            lista = new ArrayList<>();
            cn = ConexionSingleton.getConnection();
            st = cn.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {                
                pr = new Producto();
                pr.setId_producto(rs.getInt("id_producto"));
                pr.setNombre(rs.getString("nombre"));
                pr.setDescripcion(rs.getString("descripcion"));
                pr.setPrecio(rs.getDouble("precio"));
                pr.setStock(rs.getInt("stock"));
                lista.add(pr);
            }
            
        } catch (Exception e) {
            System.out.println("Error al validar user" + e.getMessage());
            try {
                cn.rollback();
            } catch (Exception ex) {
                System.out.println("error de rollback" + ex.getMessage());
            }

        } finally {
            if (cn != null) {
                try {
                } catch (Exception ex) {
                }
            }
        }
        return lista;
    }

    @Override
    public boolean insertar(Producto pro) {
        boolean flag = false;
        try {
            query = "INSERT INTO productos"
                    + "(nombre, descripcion, precio, stock, imagen)"
                    + "VALUES(?,?,?,?,?)";
            cn = ConexionSingleton.getConnection();
            st = cn.prepareStatement(query);
            st.setString(1, pro.getNombre());
            st.setString(2, pro.getDescripcion());
            st.setDouble(3, pro.getPrecio());
            st.setInt(4, pro.getStock());
            st.setString(5, pro.getImagen());
            st.executeUpdate();
            flag = true;
            
        } catch (Exception e) {
            System.out.println("Error al validar user" + e.getMessage());
            try {
                cn.rollback();
            } catch (Exception ex) {
                System.out.println("error de rollback" + ex.getMessage());
            }
            flag = false;

        } finally {
            if (cn != null) {
                try {
                } catch (Exception ex) {
                }
            }
        }
        return flag;
    }

    @Override
    public boolean update(Producto pro) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Producto searchById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean delete(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean updateStock(int id, int stock) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}

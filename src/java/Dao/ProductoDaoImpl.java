/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Interface.IProducto;
import Model.Producto;
import Util.ConexionSingleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LAB 2
 */
public class ProductoDaoImpl implements IProducto {

    private Connection cn;
    static PreparedStatement st;
    static ResultSet rs;
    static String query = null;

    @Override
    public List<Producto> lista() {
        List<Producto> lista = null;
        Producto pr;

        try {
            query = " SELECT id_producto,nombre,descripcion,precio,"
                    + " stock, imagen FROM productos; ";
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
                pr.setImagen(rs.getString("imagen"));
                lista.add(pr);
            }

        } catch (Exception e) {
            System.out.println("Error al listar" + e.getMessage());
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
            query = " INSERT INTO productos "
                    + "(nombre,descripcion,precio,stock,imagen) "
                    + " VALUES(?,?,?,?,?)";

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
            System.out.println("Error al listar" + e.getMessage());
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
        boolean flag = false;
        try {
            query = "UPDATE productos SET nombre=?,descripcion=?,precio=?,"
                    + "stock=?,imagen=? WHERE id_producto=? ";

            cn = ConexionSingleton.getConnection();
            st = cn.prepareStatement(query);
            st.setString(1, pro.getNombre());
            st.setString(2, pro.getDescripcion());
            st.setDouble(3, pro.getPrecio());
            st.setInt(4, pro.getStock());
            st.setString(5, pro.getImagen());
            st.setInt(6, pro.getId_producto());
            st.executeUpdate();
            flag = true;

        } catch (Exception e) {
            System.out.println("Error al listar" + e.getMessage());
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
    public Producto searchById(int id) {
        Producto pr =null;
        try {
            query = " SELECT * FROM productos WHERE id_producto=?;";
            cn = ConexionSingleton.getConnection();
            st = cn.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                pr = new Producto();
                pr.setId_producto(rs.getInt("id_producto"));
                pr.setNombre(rs.getString("nombre"));
                pr.setDescripcion(rs.getString("descripcion"));
                pr.setPrecio(rs.getDouble("precio"));
                pr.setStock(rs.getInt("stock"));
                pr.setImagen(rs.getString("imagen"));
            }

        } catch (Exception e) {
            System.out.println("Error al listar" + e.getMessage());
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
        return pr;
    }

    @Override
    public boolean delete(int id) {
        boolean flag = false;
        try {
            query = "DELETE FROM productos WHERE id_producto=? ";

            cn = ConexionSingleton.getConnection();
            st = cn.prepareStatement(query);
            st.setInt(1, id);
            st.executeUpdate();
            flag = true;

        } catch (Exception e) {
            System.out.println("Error al actualizar stock" + e.getMessage());
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
    public boolean updateStock(int id, int stock) {
         boolean flag = false;
        try {
            query = "UPDATE productos SET stock=? WHERE id_producto=? ";

            cn = ConexionSingleton.getConnection();
            st = cn.prepareStatement(query);
            st.setInt(1, stock);
            st.setInt(2, id);
            st.executeUpdate();
            flag = true;

        } catch (Exception e) {
            System.out.println("Error al actualizar stock" + e.getMessage());
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

}

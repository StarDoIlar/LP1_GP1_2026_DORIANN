/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Test;

import Dao.ProductoDaoImpl;
import Interface.IProducto;
import Model.Producto;
import java.util.List;

/**
 *
 * @author LAB 2
 */
public class TestProducto {

    public static IProducto dao = new ProductoDaoImpl();

    public static void main(String[] args) {
          TestProducto t = new TestProducto();
         // t.listar();
          //t.insertar();
        //  t.update();
       // t.updateStock();
        //t.SearchById();
        //t.delete();
    }

    public static void listar() {
        List<Producto> lista = dao.lista();
        if (lista != null && !lista.isEmpty()) {
            System.out.println("ID\tNombre\tPrecio\tStock");
            for (Producto p : lista) {
                System.out.println(p.getId_producto()
                        + "\t" + p.getNombre() + "\t$"
                        + p.getPrecio() + "\t"
                        + p.getStock());
            }
        } else {
            System.out.println("no hay data");
        }
    }
    
    public static void insertar(){
        Producto p = new Producto();
        
        p.setNombre("Papas");
        p.setDescripcion("Huayro");
        p.setPrecio(2);
        p.setStock(50);
        p.setImagen("/resources/img/papa.jpg");
        
        boolean result = dao.insertar(p);
        
        if (result) {
            System.out.println("Datos correctos");
        }else{
            System.out.println("DatosIncorrectos");
        } 
    }
    
     public static void update(){
        Producto p = new Producto();
        
        p.setNombre("Camotes");
        p.setDescripcion("Morado");
        p.setPrecio(4);
        p.setStock(100);
        p.setImagen("/resources/img/camote.jpg");
        p.setId_producto(3);
        boolean result = dao.update(p);
        
        if (result) {
            System.out.println("Datos correctos");
        }else{
            System.out.println("DatosIncorrectos");
        } 
    }
     
      public static void updateStock(){
        Producto p = new Producto();
        boolean result = dao.updateStock(3,500);
        
        if (result) {
            System.out.println("Stock actualizado");
        }else{
            System.out.println("DatosIncorrectos");
        } 
    }
      
      public static void SearchById(){
        Producto pr = dao.searchById(3);
          
          if (pr !=null) {
              System.out.println("id"+pr.getId_producto());
              System.out.println("nombre"+pr.getNombre());
              System.out.println("description"+pr.getDescripcion());
              System.out.println("stock"+pr.getStock());
              System.out.println("precio"+pr.getPrecio());
          }else{
            System.out.println("No hay datos");
        } 
    }
      
       public static void delete(){
        boolean result = dao.delete(3);
          
          if (result) {
              System.out.println("Eliminado");
          }else{
            System.out.println("No se puedo eliminar");
        } 
    }
}

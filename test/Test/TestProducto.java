
package Test;

import Dao.ProductoDaoImpl;
import Interface.IProducto;
import java.util.List;
import models.Producto;

public class TestProducto {
    public static IProducto dao = new ProductoDaoImpl();

    public static void main(String[] args) {
        TestProducto t = new TestProducto();
        t.listar();
        //t.insertar();
        //t.update();
        //t.updateStock();
        //t.searchById();
        //t.delete();
    }
    public static void listar() {
        List<Producto> lista = dao.lista();
        if (lista != null && !lista.isEmpty()) {
            System.out.println("ID\tNombre\tPrecio\tStock");
            for (Producto p : lista) {
                System.out.println(p.getId_producto()
                                   + "\t"+p.getNombre()+"\t"
                                   + p.getPrecio()+"\t"
                                   + p.getStock());
            }       
        }else {
            System.out.println("No hay data");
        }
    }
    public static void insertar() {
        Producto p = new Producto();
        p.setNombre("papas");
        p.setDescripcion("Huayro");
        p.setPrecio(2);
        p.setStock(50);
        p.setImagen("/resources/img/papa.png");
        boolean result = dao.insertar(p);
        if (result) {
            System.out.println("Datos Correctos");
        } else {
            System.out.println("Datos Incorrectos");
        }
    }
    public static void update() {
        Producto p = new Producto();
        p.setNombre("maiz");
        p.setDescripcion("Selvatico");
        p.setPrecio(1.50);
        p.setStock(34);
        p.setImagen("/resources/img/maiz.png");
        p.setId_producto(3);
        boolean result = dao.update(p);
        if (result) {
            System.out.println("Datos Correctos");
        } else {
            System.out.println("Datos Incorrectos");
        }
    }
    public static void updateStock() {
        Producto p = new Producto();
        boolean result = dao.updateStock(3, 20);
        if (result) {
            System.out.println("Stock actualizado");
        } else {
            System.out.println("Datos Incorrectos");
        }
    }
    public static void searchById() {
        Producto pr = dao.searchById(3);
        
        if (pr !=null) {
            System.out.println("id: " + pr.getId_producto());
            System.out.println("nombre: " + pr.getNombre());
            System.out.println("descripcion: " + pr.getDescripcion());
            System.out.println("precio: " + pr.getPrecio());
            System.out.println("stock: " + pr.getStock());
        } else {
            System.out.println("No hay Datos");
        }
    }
    public static void delete() {
        boolean result = dao.delete(4);
        
        if (result) {
            System.out.println("Eliminado");
        } else {
            System.out.println("No se pudo eliminar");
        }
    }
}

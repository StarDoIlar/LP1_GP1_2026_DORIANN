
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
    
}

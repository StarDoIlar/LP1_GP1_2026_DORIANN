
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
    
}

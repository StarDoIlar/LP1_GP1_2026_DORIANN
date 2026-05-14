
package Interface;

import java.util.List;
import models.Producto;

public interface IProducto {
    public List<Producto> lista();
    public boolean insertar(Producto pro);
    public boolean update(Producto pro);
    public Producto searchById(int id);
    public boolean delete(int id);
    public boolean  updateStock(int id, int stock);
}

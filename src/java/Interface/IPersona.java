
package Interface;

import java.util.List;
import models.persona;
import models.usuario;

public interface IPersona {
    public List<persona> lista();
    public int insertar(persona p, usuario u);
    public boolean update(persona p);
    public persona SearchById(int id);
    public void delete(int id);
}

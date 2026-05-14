
package Interface;

import java.util.List;
import models.Persona;
import models.Usuario;

public interface IPersona {
    public List<Persona> lista();
    public int insertar(Persona p, Usuario u);
    public boolean update(Persona p);
    public Persona SearchById(int id);
    public void delete(int id);
}

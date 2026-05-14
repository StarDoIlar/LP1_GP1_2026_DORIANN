
package Test;

import Dao.PersonaDaoImpl;
import Dao.UsuarioDaoImpl;
import Interface.IPersona;
import Interface.IUsuario;
import models.Persona;
import models.Usuario;

/**
 *
 * @author LAB 2
 */
public class TestPersona {

    IPersona dao = new PersonaDaoImpl();
    IUsuario Udao = new UsuarioDaoImpl();

    public static void main(String[] args) {
        TestPersona t = new TestPersona();
       // t.crear_usuario();
       t.validate();
    }

    public void crear_usuario() {
        Persona p = new Persona();
        p.setNombre("Jhan Arly");
        p.setEmail("jhan@gmail.com");
        p.setDireccion("upeu");
        p.setTelefono("987654321");
        Usuario u = new Usuario();
        u.setPassword("admin123");
        int result = dao.insertar(p, u);
        if (result > 0) {
            System.out.println("Usuario" + p.getEmail());
            System.out.println("Rol" + u.getRol());
        } else {
            System.out.println("No se realizo el registro");
        }
    }
    
    public void validate(){
        Usuario u =Udao.validate("jhan@gmail.com", "admin123");
        if (u !=null && u.getPersona() !=null) {
            System.out.println("Bienvenido"+u.getPersona().getNombre());
            System.out.println("Rol"+ u.getRol());
        }else{
            System.out.println("credenciales incorrectas");
        }
    }

}
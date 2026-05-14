
package Test;

import Dao.PersonaDaoImpl;
import Dao.UsuarioDaoImpl;
import Interface.IPersona;
import Interface.IUsuario;
import models.Persona;
import models.Usuario;

public class TestPersona {
    IPersona dao = new PersonaDaoImpl();
    IUsuario udao = new UsuarioDaoImpl();
    
    public static void main(String[]args){
 
        TestPersona test = new TestPersona();
        test.crear_usuario();
        //test.validate();
    }

    public void crear_usuario() {
        Persona p = new Persona();
        p.setNombre("Doriann Gonzales");
        p.setEmail("dorianngonzales123@gmail.com");
        p.setTelefono("984371752");
        p.setDireccion("upeu");
        
        Usuario u = new Usuario();
        u.setPassword("admin123");
        
        int result = dao.insertar(p, u);
        
        if (result > 0) {
            System.out.println("Usuario: " + p.getEmail());
            System.out.println("Rol: " + u.getRol());
        } else {
            System.out.println("No se realizó el registro");
        }
    }
    public void validate(){
        Usuario u = udao.validate("dorianngonzales123@gmail.com", "admin123");
        if (u != null && u.getPersona() != null) {
            System.out.println("Bienvenido" +u.getPersona().getNombre());
            System.out.println("Rol "+u.getRol());
        } else {
            System.out.println("Credenciales incorrectas");
        }
    }
}
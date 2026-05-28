
package Controllers;

import Dao.PersonaDaoImpl;
import Dao.UsuarioDaoImpl;
import Interface.IPersona;
import Interface.IUsuario;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Persona;
import models.Usuario;

@WebServlet(name = "AuthController", urlPatterns = {"/AuthController"})
public class AuthController extends HttpServlet {
    
    private final IUsuario uDao = new UsuarioDaoImpl();
    private final IPersona pDao = new PersonaDaoImpl();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AuthController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AuthController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String action = request.getParameter("action");
        JsonObject jsonResponsive = new JsonObject();
        
        Gson gson = new Gson();
        
        try (PrintWriter out = response.getWriter()){
            
            if (action.equals("validar")) {
                String user = request.getParameter("usuario");
                String pass = request.getParameter("password");
                Usuario us = uDao.validate(user, pass);
                if (us != null && us.getUsuario() !=null) {
                    HttpSession session = request.getSession(true);
                    session.setAttribute("usuario", us);
                    jsonResponsive.addProperty("success", true);
                    jsonResponsive.addProperty("message", "Inicio de Sesion");
                    
                    jsonResponsive.add("userData", gson.toJsonTree(us));
                } else {
                    jsonResponsive.addProperty("success", false);
                    jsonResponsive.addProperty("message", "Usuario o contraseña invalida");
                }
                out.print(jsonResponsive.toString());
                
            }else if(action.equals("salir")) {
                HttpSession session = request.getSession();
                if (session !=null) session.invalidate(); {
                }
                jsonResponsive.addProperty("success", true);
                jsonResponsive.addProperty("message", "Sesion Cerrada");
                out.print(jsonResponsive.toString());
                
                
            } else if (action.equals("register")){
                Persona p = new Persona();
                Usuario u = new Usuario();
                
                p.setNombre(request.getParameter("nombre"));
                p.setEmail(request.getParameter("email"));
                p.setTelefono(request.getParameter("telefono"));
                p.setDireccion(request.getParameter("direccion"));
                u.setPassword(request.getParameter("password"));
                
                int resultado = pDao.insertar(p, u);
                
                jsonResponsive.addProperty("success", resultado !=0);
                jsonResponsive.addProperty("message", resultado !=0 ? "Registro Success":"Error de registro");
                out.print(jsonResponsive.toString());
            }
            
            
        } catch (Exception e) {
            response.setStatus(500);
            jsonResponsive.addProperty("success", false);
            jsonResponsive.addProperty("message", "Error "+e.getMessage());
            response.getWriter().print(jsonResponsive.toString());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

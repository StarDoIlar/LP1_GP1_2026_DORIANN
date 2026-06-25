
package util;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import models.Usuario;
import models.rol;

@WebFilter(urlPatterns = {"/adminproductos.html","/ProductoController"})
public class AdminFilter implements Filter{

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) sr;
        HttpServletResponse res = (HttpServletResponse) sr1;
        HttpSession session = req.getSession(false);
        
        Usuario user = (session != null)? (Usuario) session.getAttribute("usuario"):null;
        String url = req.getRequestURI();
        
        if (url.contains("adminproductos.html")|| url.contains("ProductoController")) {
            if (user != null && user.getRol() == rol.ADMIN) {
                fc.doFilter(sr, sr1);
            } else {
                res.sendRedirect("index.html");
            }
        }
        
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Dao.PedidoDaoImpl;
import Dao.ProductoDaoImpl;
import Interface.IPedido;
import Interface.IProducto;
import Model.Carrito;
import Model.EstadoPedido;
import Model.Pedido;
import Model.Producto;
import Model.Usuario;
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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LAB 2
 */
@WebServlet(name = "AppController", urlPatterns = {"/AppController"})
public class AppController extends HttpServlet {

    private IProducto pDao = new ProductoDaoImpl();
    private IPedido IDao = new PedidoDaoImpl();
    private Gson gson = new Gson();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        JsonObject jsonResponse = new JsonObject();
        HttpSession session = request.getSession();

        List<Carrito> listCarrito = (List<Carrito>) session.getAttribute("carrito");
        if (listCarrito == null) {
            listCarrito = new ArrayList<>();
            session.setAttribute("carrito", listCarrito);
        }

        try (PrintWriter out = response.getWriter()) {
            switch (action) {
                case "listarProductos":
                    List<Producto> productos = pDao.lista();
                    out.print(gson.toJson(productos));
                    break;

                case "AddCarrito":
                    int id = Integer.parseInt(request.getParameter("id"));
                    Producto p = pDao.searchById(id);
                    if (p != null) {
                        int pos = -1;
                        for (int i = 0; i < listCarrito.size(); i++) {
                            if (listCarrito.get(i).getIdProducto() == id) {
                                pos = i;
                                break;
                            }
                        }
                        if (pos != -1) {
                            int nuevaCant = listCarrito.get(pos).getCantidad() + 1;
                            listCarrito.get(pos).setCantidad(nuevaCant);
                            listCarrito.get(pos).setSubTotal(nuevaCant * p.getPrecio());
                        } else {
                            Carrito car = new Carrito();
                            car.setIdProducto(p.getId_producto());
                            car.setNombre(p.getNombre());
                            car.setPrecioCompra(p.getPrecio());
                            car.setCantidad(1);
                            car.setSubTotal(p.getPrecio());
                            listCarrito.add(car);
                        }
                        jsonResponse.addProperty("success", true);
                        jsonResponse.addProperty("cartCout", listCarrito.size());
                    }
                    out.print(jsonResponse.toString());

                    break;

                case "listarCarrito":
                    double total = listCarrito.stream().mapToDouble(Carrito::getSubTotal).sum();
                    session.setAttribute("total", total);
                    JsonObject carData = new JsonObject();

                    carData.add("items", gson.toJsonTree(listCarrito));
                    carData.addProperty("total", total);
                    out.print(carData.toString());

                    break;
                case "Delete":
                    try {
                        int idproducto = Integer.parseInt(request.getParameter("id"));
                        boolean eliminado = listCarrito.removeIf(c -> c.getIdProducto() == idproducto);
                        session.setAttribute("carrito", listCarrito);
                        jsonResponse.addProperty("success", eliminado);
                        jsonResponse.addProperty("message", eliminado ? "Producto elimnado"
                                : "No se encontro el producto ");

                    } catch (Exception e) {
                        jsonResponse.addProperty("success", false);
                        jsonResponse.addProperty("message", "Error" + e.getMessage());
                    }
                    out.print(jsonResponse.toString());
                    break;
                case "GenerarCompra":
                    Usuario user = (Usuario) session.getAttribute("usuario");

                    //validar Sesion
                    if (user == null) {
                        jsonResponse.addProperty("success", false);
                        jsonResponse.addProperty("message", "Debe iniciar Sesion");
                        out.print(jsonResponse.toString());
                        return;
                    }
                    //validar carrito vacio
                    if (listCarrito == null || listCarrito.isEmpty()) {
                        jsonResponse.addProperty("success", false);
                        jsonResponse.addProperty("message", "El carrito esta vacio");
                        out.print(jsonResponse.toString());
                        return;
                    }
                    //validar Stock
                    boolean stockDisponible = true;
                    String productoSinStock = "";

                    for (Carrito c : listCarrito) {
                        Producto proDB = pDao.searchById(c.getIdProducto());
                        if (proDB.getStock() < c.getCantidad()) {
                            stockDisponible = false;
                            productoSinStock = proDB.getNombre();
                            break;
                        }
                    }
                    
                     if (!stockDisponible) {
                        jsonResponse.addProperty("success", false);
                        jsonResponse.addProperty("message", "Stock insuficiente" + productoSinStock);
                        out.print(jsonResponse.toString());
                        return;
                    }
                    //preparar el pedido
                    double totalPagar = listCarrito.stream().mapToDouble(Carrito::getSubTotal).sum();
                    Pedido pedido = new Pedido();
                    pedido.setPersona(user.getPersona());
                    pedido.setTotal(totalPagar);
                    pedido.setEstadoPedido(EstadoPedido.ENVIADO);
                    pedido.setDetallePedido(listCarrito);
                    //guardar el pedido
                    int idGenerado = IDao.generarPedido(pedido);

                    if (idGenerado > 0) {
                        for (Carrito c : listCarrito) {
                            Producto prodDB = pDao.searchById(c.getIdProducto());
                            int nuevoStock = prodDB.getStock() - c.getCantidad();
                            pDao.updateStock(c.getIdProducto(), nuevoStock);
                        }
                        listCarrito.clear();
                        session.setAttribute("carrito", listCarrito);
                        session.setAttribute("total", 0.0);
                        jsonResponse.addProperty("success", true);
                        jsonResponse.addProperty("message", "Compra realizada con exito");
                    } else {
                        jsonResponse.addProperty("success", false);
                        jsonResponse.addProperty("message", "Error al procesar el pedido");
                    }
                    out.print(jsonResponse.toString());
                    break;

                default:
                    throw new AssertionError();
            }

        } catch (Exception e) {
            System.out.println("error" + e.getMessage());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

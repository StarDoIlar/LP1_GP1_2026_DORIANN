/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Dao.PedidoDaoImpl;
import Dao.ProductoDaoImpl;
import Interface.IPedido;
import Interface.IProducto;
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
import models.Carrito;
import models.EstadoPedido;
import models.Pedido;
import models.Producto;
import models.Usuario;

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
                        jsonResponse.addProperty("cartCount", listCarrito.size());
                    }
                    out.print(jsonResponse.toString());
                    break;

                case "listarCarrito":
                    double total = listCarrito.stream().mapToDouble(Carrito::getSubTotal).sum();
                    session.setAttribute("total", total);
                    JsonObject cartData = new JsonObject();
                    cartData.add("item", gson.toJsonTree(listCarrito));
                    cartData.addProperty("total", total);
                    out.print(cartData.toString());
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
                    if (user == null) {
                        jsonResponse.addProperty("success", false);
                        jsonResponse.addProperty("message", "Inicie Sesion");
                        out.print(jsonResponse.toString());
                    }
                    if (listCarrito == null || listCarrito.isEmpty()) {
                        jsonResponse.addProperty("success", false);
                        jsonResponse.addProperty("message", "El carrito vacio");
                        out.print(jsonResponse.toString());
                    }

                    boolean stockDiponible = true;
                    String productoSinStock = "";

                    for (Carrito c : listCarrito) {
                        Producto prodBD = pDao.searchById(c.getIdProducto());
                        if (prodBD.getStock() < c.getCantidad()) {
                            stockDiponible = false;
                            productoSinStock = prodBD.getNombre();
                            break;
                        }
                    }
                    if (!stockDiponible) {
                        jsonResponse.addProperty("success", false);
                        jsonResponse.addProperty("message", "Stock insuficiente" + productoSinStock);
                        out.print(jsonResponse.toString());
                        return;
                    }

                    double totalPagar = listCarrito.stream().mapToDouble(Carrito::getSubTotal).sum();

                    Pedido pedido = new Pedido();
                    pedido.setPersona(user.getPersona());
                    pedido.setTotal(totalPagar);
                    pedido.setEstadopedido(EstadoPedido.ENVIADO);
                    pedido.setDetallePedido(listCarrito);

                    int idGenerado = IDao.generarPedido(pedido);
                    if (idGenerado > 0) {
                        for (Carrito c : listCarrito) {
                            Producto prodBD = pDao.searchById(c.getIdProducto());
                            int nuevoStock = prodBD.getStock() - c.getCantidad();
                            pDao.updateStock(c.getIdProducto(), nuevoStock);

                        }
                        listCarrito.clear();
                        session.setAttribute("carrito", listCarrito);
                        session.setAttribute("total", 0.0);
                        jsonResponse.addProperty("success", true);
                        jsonResponse.addProperty("message", "Compra exitosa !!!");
                    } else {
                        jsonResponse.addProperty("success", false);
                        jsonResponse.addProperty("message", "Error al procesar el pedido");
                    }
                    out.print(jsonResponse.toString());

                    break;

                default:
                    jsonResponse.addProperty("success", false);
                    jsonResponse.addProperty("message", "accion no encontrada");
                    out.print(jsonResponse.toString());
            }
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
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}

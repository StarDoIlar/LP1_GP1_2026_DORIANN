/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

$(document).ready(function(){
    cargarProductos();
});

function cargarProductos(){
    fetch('AppController?action=listarProductos')
            .then(res=> res.json())
            .then(productos =>{
                const contenedor = $('lista-productos');
             contenedor.empty();
             productos.forEach(p =>{
                 contenedor.append(`
                   <div class="col-12 col-sm-6 col-md-4 col-lg-3">
                <div class="card h-100 shadow-sm border-0">
                    <img src="${p.imagen}" alt="${p.nombre}" class="car-img-top p-2"/>
                    <div class="card-body d-flex flex-column">
                        <h6 class="card-title fw-bold">${p.nombre}</h6>
                        <p class="card-text text-muted small flex-grow-1">${p.descripcion}</p>
                        <div class="d-flex justify-content-between align-items-center mt-3">
                            <span class="fs-5 fw-bold text-info">${p.precio.toFixed(2)}</span>
                            <span class="badge ${p.stock >0} 'bg-light text-success':
                                         'bg-light text-danger'}">${p.stock}</span> 
                        </div>
                        <button onclick="agregarCarrito(${p.id_producto})" class="btn btn-info text-white w-100 mt-3">
                            <i class="bi bi-cart-plus me-2">Agregar</i>
                        </button>
                    </div> 
                </div>
            </div> `);
                 
             });
    }).catch (err=> console.log("Error al cargar productos",err));   
    }
    
    function agregarCarrito(id){
        console.log(id);
          fetch(`AppController?action=AddCarrito&id=${id}`, {method: 'POST'})
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    $('#cart-count').text(data.cartCount);
                    Swal.fire({
                        toast: true,
                        position: 'top-end',
                        icon: 'success',
                        title: 'Producto agregado',
                        showConfirmButton: false,
                        timer: 1500
                    });
                }
            });
        
    }
    function actualizarContadorCarrito(){
        fetch('AppController?action=listarCarrito')
                .then(res=> res.json())
                .then(data=>{
                    $('#cart-count').text(data.items ? data.items.length:0);
        });
    }
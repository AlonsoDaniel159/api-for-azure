package com.alonso.firstapi.azure.service;

import com.alonso.firstapi.azure.entity.Producto;
import com.alonso.firstapi.azure.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductoService {
    
    private final ProductoRepository productoRepository;
    
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }
    
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
    }
    
    public Producto crear(Producto producto) {
        return productoRepository.save(producto);
    }
    
    public Producto actualizar(Long id, Producto productoActualizado) {
        Producto producto = obtenerPorId(id);
        
        if (productoActualizado.getNombre() != null) {
            producto.setNombre(productoActualizado.getNombre());
        }
        if (productoActualizado.getDescripcion() != null) {
            producto.setDescripcion(productoActualizado.getDescripcion());
        }
        if (productoActualizado.getPrecio() != null) {
            producto.setPrecio(productoActualizado.getPrecio());
        }
        if (productoActualizado.getCantidad() != null) {
            producto.setCantidad(productoActualizado.getCantidad());
        }
        
        return productoRepository.save(producto);
    }
    
    public void eliminar(Long id) {
        Producto producto = obtenerPorId(id);
        productoRepository.delete(producto);
    }
    
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }
}


package com.alonso.firstapi.azure.config;

import com.alonso.firstapi.azure.entity.Producto;
import com.alonso.firstapi.azure.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductoRepository productoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Solo insertar si la tabla está vacía
        if (productoRepository.count() == 0) {
            productoRepository.save(new Producto(null, "Laptop Dell",
                "Laptop Dell XPS 15, procesador Intel i7, 16GB RAM",
                new BigDecimal("1299.99"), 5, null, null));

            productoRepository.save(new Producto(null, "Mouse Logitech",
                "Mouse inalambrico Logitech MX Master 3",
                new BigDecimal("99.99"), 15, null, null));

            productoRepository.save(new Producto(null, "Teclado Mecanico",
                "Teclado mecanico RGB Corsair K95",
                new BigDecimal("199.99"), 8, null, null));

            productoRepository.save(new Producto(null, "Monitor LG",
                "Monitor 4K LG 27 pulgadas, 60Hz, USB-C",
                new BigDecimal("499.99"), 3, null, null));

            productoRepository.save(new Producto(null, "Cable HDMI",
                "Cable HDMI 2.1 de 2 metros",
                new BigDecimal("29.99"), 50, null, null));

            productoRepository.save(new Producto(null, "Auriculares Sony",
                "Auriculares inalambricos Sony WH-1000XM4",
                new BigDecimal("349.99"), 12, null, null));
        }
    }
}


package com.afriasdev.aiassistant.webflux.app.models.services;
import org.springframework.stereotype.Service;

import com.afriasdev.aiassistant.webflux.app.models.dao.ProductoDao;
import com.afriasdev.aiassistant.webflux.app.models.documents.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service 
public class ProductoServiceImpl implements ProductoService {

    private final ProductoDao productoDao;

    public ProductoServiceImpl(ProductoDao productoDao) {
        this.productoDao = productoDao;
    }

    @Override
    public Flux<Producto> findAll() {
        return productoDao.findAll();
    }

    @Override
    public Flux<Producto> findAllUpperCase() {
        return productoDao.findAll().map(producto -> {
            producto.setNombre(producto.getNombre().toUpperCase());
            return producto;
        });
    }

    @Override
    public Flux<Producto> findAllUpperCaseRepeat() {
        return findAllUpperCase().repeat(5000);
    }

    @Override
    public Mono<Producto> findById(String id) {
        return productoDao.findById(id);
    }

    @Override
    public Mono<Producto> save(Producto producto) {
        return productoDao.save(producto);
    }

    @Override
    public Mono<Void> delete(Producto producto) {
        return productoDao.delete(producto);
    }

}

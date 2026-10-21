package com.afriasdev.aiassistant.webflux.app.models.services;
import com.afriasdev.aiassistant.webflux.app.models.documents.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {

    public Flux<Producto> findAll();

    public Flux<Producto> findAllUpperCase();

    public Flux<Producto> findAllUpperCaseRepeat();

    public Mono<Producto> findById(String id);

    public Mono<Producto> save(Producto producto);

    public Mono<Void> delete(Producto producto);
    
}

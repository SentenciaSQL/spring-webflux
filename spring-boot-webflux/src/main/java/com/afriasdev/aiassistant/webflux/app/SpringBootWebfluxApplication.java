package com.afriasdev.aiassistant.webflux.app;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.afriasdev.aiassistant.webflux.app.models.dao.ProductoDao;
import com.afriasdev.aiassistant.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApplication.class);

	private final ProductoDao dao;
	private final ReactiveMongoTemplate mongoTemplate;

	public SpringBootWebfluxApplication(ProductoDao dao, ReactiveMongoTemplate mongoTemplate) {
		this.dao = dao;
		this.mongoTemplate = mongoTemplate;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		mongoTemplate.dropCollection("productos").subscribe();

		Flux.just(new Producto("TV TCL", 456.99), new Producto("Laptop Dell", 899.99),
				new Producto("Smartphone Samsung", 699.99), new Producto("Tablet Apple", 499.99),
				new Producto("Monitor LG", 299.99), new Producto("Keyboard Logitech", 49.99),
				new Producto("Mouse Razer", 59.99)).flatMap(producto -> {
					producto.setCreateAt(new Date());
					return dao.save(producto);
				})
				.subscribe(producto -> log.info("Saved: " + producto.getId() + " - " + producto.getNombre()));

	}
}

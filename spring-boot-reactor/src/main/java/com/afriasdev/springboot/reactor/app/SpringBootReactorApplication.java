package com.afriasdev.springboot.reactor.app;

import com.afriasdev.springboot.reactor.app.models.Usuario;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(SpringBootReactorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        //ejemploIterable();
        ejemploFlatMap();
    }

    public void ejemploFlatMap() throws Exception {

        List<String> usuariosList = new ArrayList<>();
        usuariosList.add("Andres, Frias");
        usuariosList.add("Juan, Gonzales");
        usuariosList.add("pedro, duran");
        usuariosList.add("maria, guzman");
        usuariosList.add("jose, guzman");
        usuariosList.add("luis, soto");
        usuariosList.add("ana, diaz");
        usuariosList.add("carmen, mercedez");
        usuariosList.add("jorge, casado");
        usuariosList.add("sofia, sterling");

        Flux.fromIterable(usuariosList)
                .map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
                .flatMap(usuario -> {
                    if (usuario.getNombre().equalsIgnoreCase("andres")) {
                        return Mono.just(usuario);
                    } else {
                        return Mono.empty();
                    }
                }).map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                }).subscribe(u -> log.info(u.toString()));
    }

    public void ejemploIterable() throws Exception {

        List<String> usuariosList = new ArrayList<>();
        usuariosList.add("andres, frias");
        usuariosList.add("juan, gonzales");
        usuariosList.add("pedro, duran");
        usuariosList.add("maria, guzman");
        usuariosList.add("jose, guzman");
        usuariosList.add("luis, soto");
        usuariosList.add("ana, diaz");
        usuariosList.add("carmen, mercedez");
        usuariosList.add("jorge, casado");
        usuariosList.add("sofia, sterling");

        Flux<String> nombres = Flux.fromIterable(usuariosList);
        // Flux.just("Andres Frias", "Juan Gonzales", "Pedro Duran", "Maria Guzman", "Jose Guzman", "Luis Soto", "Ana Diaz", "Carmen Mercedez", "Jorge Casado", "Sofia Sterling");

        Flux<Usuario> usuarios = nombres.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
                .filter(usuario -> usuario.getNombre().toLowerCase().equals("andres") || usuario.getNombre().toLowerCase().equals("juan"))
                .doOnNext(usuario -> {
                    if (usuario == null) {
                        throw new RuntimeException("El elemento no puede estar vacío");
                    }

                    System.out.println("Nombre: " + usuario.getNombre().concat(" ").concat(usuario.getApellido()));

                }).map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                });

        usuarios.subscribe(e -> log.info(e.toString()), error -> log.error(error.getMessage()),
                new Runnable() {
                    @Override
                    public void run() {
                        log.info("Ha finalizado la ejecución del observable");
                    }
                }
        );
    }
}

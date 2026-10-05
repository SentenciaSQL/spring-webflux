package com.afriasdev.springboot.reactor.app;

import com.afriasdev.springboot.reactor.app.models.Comentarios;
import com.afriasdev.springboot.reactor.app.models.Usuario;
import com.afriasdev.springboot.reactor.app.models.UsuarioComentarios;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
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
        //ejemploFlatMap();
        //ejemploToString();
        //ejemploCollectList();
        //ejemploUsuarioCometriosFlatMap();
        //ejemploUsuarioCometriosZipWith();
        //ejemploUsuarioCometriosZipWithForma2();
        //ejemploZipWithRangos();
        ejemploInterval();
    }

    private Usuario parseUsuario(String nombreCompleto) {
        String[] datos = nombreCompleto.split(",");
        if (datos.length < 2) {
            throw new IllegalArgumentException("Formato inválido: " + nombreCompleto);
        }
        return new Usuario(datos[0].trim().toUpperCase(), datos[1].trim().toUpperCase());
    }

    public Usuario crearUsuario() {
        log.info("Creando usuario...");
        return new Usuario("Andres", "Frias");
    }

    public void ejemploInterval() {
        Flux<Integer> rango = Flux.range(1, 12);
        Flux<Long> retraso = Flux.interval(Duration.ofSeconds(1));

        rango.zipWith(retraso, (r, t) -> r)
                .doOnNext(i -> log.info(i.toString()))
                .blockLast();
    }

    public void ejemploZipWithRangos() {
        Flux.just(1, 2, 3, 4)
                .map(i -> (i * 2))
                .zipWith(Flux.range(0, 4), (uno, dos) -> String.format("Primer Flux: %d, Segundo Flux: %d", uno, dos))
                .subscribe(texto -> log.info(texto));
    }

    public void ejemploUsuarioCometriosZipWithForma2() {
        Mono<Usuario> usuarioMono = Mono.fromCallable(() -> crearUsuario());

        Mono<Comentarios> comentariosUsuarioMono = Mono.fromCallable(() -> {
            Comentarios comentarios = new Comentarios();
            comentarios.addComentario("Hola, que tal");
            comentarios.addComentario("Que bueno tu curso");
            comentarios.addComentario("Estoy aprendiendo mucho");
            return comentarios;
        });

        Mono<UsuarioComentarios> usuarioComentarios =  usuarioMono
                .zipWith(comentariosUsuarioMono)
                .map(tuple -> new UsuarioComentarios(tuple.getT1(), tuple.getT2()));

        usuarioComentarios.subscribe(uc -> log.info(uc.toString()));
    }

    public void ejemploUsuarioCometriosZipWith() {
        Mono<Usuario> usuarioMono = Mono.fromCallable(() -> crearUsuario());

        Mono<Comentarios> comentariosUsuarioMono = Mono.fromCallable(() -> {
            Comentarios comentarios = new Comentarios();
            comentarios.addComentario("Hola, que tal");
            comentarios.addComentario("Que bueno tu curso");
            comentarios.addComentario("Estoy aprendiendo mucho");
            return comentarios;
        });

        Mono<UsuarioComentarios> usuarioComentarios =  usuarioMono
                .zipWith(comentariosUsuarioMono, (usuario, comentarios) -> new UsuarioComentarios(usuario, comentarios));

        usuarioComentarios.subscribe(uc -> log.info(uc.toString()));
    }

    public void ejemploUsuarioCometriosFlatMap() {
        Mono<Usuario> usuarioMono = Mono.fromCallable(() -> crearUsuario());

        Mono<Comentarios> comentariosUsuarioMono = Mono.fromCallable(() -> {
            Comentarios comentarios = new Comentarios();
            comentarios.addComentario("Hola, que tal");
            comentarios.addComentario("Que bueno tu curso");
            comentarios.addComentario("Estoy aprendiendo mucho");
            return comentarios;
        });

        usuarioMono.flatMap(u -> comentariosUsuarioMono.map(c -> new UsuarioComentarios(u, c)))
                .subscribe(uc -> log.info(uc.toString()));
    }

    public void ejemploCollectList() throws Exception {

        List<Usuario> usuariosList = new ArrayList<>();
        usuariosList.add(new Usuario("Andres", "Frias"));
        usuariosList.add(new Usuario("Juan", "Gonzales"));
        usuariosList.add(new Usuario("pedro", "duran"));
        usuariosList.add(new Usuario("maria", "guzman"));
        usuariosList.add(new Usuario("jose", "guzman"));
        usuariosList.add(new Usuario("luis", "soto"));
        usuariosList.add(new Usuario("ana", "diaz"));
        usuariosList.add(new Usuario("carmen", "mercedez"));
        usuariosList.add(new Usuario("jorge", "casado"));
        usuariosList.add(new Usuario("sofia", "sterling"));

        Flux.fromIterable(usuariosList)
                .collectList()
                .subscribe(lista -> {
                    lista.forEach(usuario -> log.info(usuario.toString()));
                });
    }

    public void ejemploToString() throws Exception {

        List<Usuario> usuariosList = new ArrayList<>();
        usuariosList.add(new Usuario("Andres", "Frias"));
        usuariosList.add(new Usuario("Juan", "Gonzales"));
        usuariosList.add(new Usuario("pedro", "duran"));
        usuariosList.add(new Usuario("maria", "guzman"));
        usuariosList.add(new Usuario("jose", "guzman"));
        usuariosList.add(new Usuario("luis", "soto"));
        usuariosList.add(new Usuario("ana", "diaz"));
        usuariosList.add(new Usuario("carmen", "mercedez"));
        usuariosList.add(new Usuario("jorge", "casado"));
        usuariosList.add(new Usuario("sofia", "sterling"));

        Flux.fromIterable(usuariosList)
                .map(usuario -> usuario.getNombre().toUpperCase().concat(usuario.getApellido().toUpperCase()))
                .flatMap(nombre -> {
                    if (nombre.contains("andres".toUpperCase())) {
                        return Mono.just(nombre);
                    }
                    return Mono.empty();
                })
                .map(nombre -> {
                    return nombre.toLowerCase();
                })
                .subscribe(u -> log.info("Usuario: {}", u));
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
                .map(this::parseUsuario)
                .flatMap(usuario -> {
                    if (usuario.getNombre().equalsIgnoreCase("andres")) {
                        return Mono.just(usuario);
                    }
                    return Mono.empty();
                })
                .map(usuario -> {
                    usuario.setNombre(usuario.getNombre().toLowerCase());
                    return usuario;
                })
                .subscribe(u -> log.info("Usuario: {}", u));
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

        Flux<Usuario> usuarios = nombres
                .map(this::parseUsuario)
                .filter(usuario -> usuario.getNombre().equalsIgnoreCase("andres")
                        || usuario.getNombre().equalsIgnoreCase("juan"))
                .doOnNext(usuario -> {
                    if (usuario == null) {
                        throw new RuntimeException("El elemento no puede estar vacío");
                    }
                    log.info("Nombre: {} {}", usuario.getNombre(), usuario.getApellido());
                })
                .map(usuario -> {
                    usuario.setNombre(usuario.getNombre().toLowerCase());
                    return usuario;
                });

        usuarios.subscribe(
                e -> log.info("Usuario emitido: {}", e),
                error -> log.error("Error: {}", error.getMessage()),
                () -> log.info("Ha finalizado la ejecución del observable")
        );
    }
}

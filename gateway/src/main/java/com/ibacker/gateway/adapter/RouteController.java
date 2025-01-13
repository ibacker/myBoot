package com.ibacker.gateway.adapter;

import com.ibacker.gateway.DynamicRouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping("/route")
public class RouteController {

    private final DynamicRouteService dynamicRouteService;

    @Autowired
    public RouteController(DynamicRouteService dynamicRouteService) {
        this.dynamicRouteService = dynamicRouteService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshRoutes() {
        dynamicRouteService.updateRoutes();
        return ResponseEntity.ok("Routes refreshed successfully!");
    }

    @GetMapping("/test")
    public Mono<ResponseEntity> testFluxAndMono() {


        Mono.just("test").map(String::toUpperCase).subscribe(System.out::println);

        Flux<String> characterFlux = Flux.just("a", "b", "c")
                .map(String::toUpperCase)
                .filter(s -> s.startsWith("A"));

        Stream<String> fruitStream = Stream.of("apple", "banana", "cherry");
        Flux<String> characterFlux2 = Flux.fromStream(fruitStream);


        Flux<Tuple2<String, String>> zippedFlux = Flux.zip(characterFlux, characterFlux2);

        zippedFlux.map(tuple -> tuple.getT1() + tuple.getT2()).subscribe(System.out::println);

        Flux.just("one", "two", "three", "four", "five")
                .flatMap(e->Mono.just(e).map(e1-> {
                    return  e1 + " Mono";}).subscribeOn(Schedulers.parallel()))
                .subscribe(System.out::println);

        Flux.just("one", "two", "three", "four", "five").any(e->e.equals("one")).subscribe(System.out::println);

        return Mono.just(ResponseEntity.ok("test"));
    }
}

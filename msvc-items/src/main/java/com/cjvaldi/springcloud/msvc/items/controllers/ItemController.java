package com.cjvaldi.springcloud.msvc.items.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.cjvaldi.springcloud.msvc.items.models.Item;
import com.cjvaldi.springcloud.msvc.items.models.Product;
import com.cjvaldi.springcloud.msvc.items.services.ItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ItemController {

    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    private final ItemService service;
    private final CircuitBreakerFactory cBreakerFactory;

    // esta anotación @Qualifier("ItemServiceWebClient") es para inyectar el
    // servicio que implementa la interfaz ItemService, en este caso el
    // ItemServiceWebClient, ya que hay otra implementación ItemServiceFeing

    public ItemController(@Qualifier("itemServiceWebClient") ItemService service,
            CircuitBreakerFactory cBreakerFactory) {
        this.service = service;
        this.cBreakerFactory = cBreakerFactory;
    }

    @GetMapping
    public List<Item> list(@RequestParam(name = "name", required = false) String name,
            @RequestHeader(name = "token-request", required = false) String token) {
        System.out.println("name: " + name);
        System.out.println("token-request: " + token);
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> details(@PathVariable Long id) {
        Optional<Item> itemOptional = cBreakerFactory.create("items").run(() -> service.findById(id), e -> {
            System.out.println(e.getMessage());
            logger.error(e.getMessage());

            Product product = new Product();
            product.setCreateAt(LocalDate.now());
            product.setId(1L);
            product.setName("Camara Sony - CortoCircuito");
            product.setPrice(500.00);
            return Optional.of(new Item(product, 5));
        });

        if (itemOptional.isPresent()) {
            return ResponseEntity.ok(itemOptional.get());
        }
        return ResponseEntity.status(404)
                .body(Collections.singletonMap("message", "Producto no existe en el microservicio msvc-products"));
    }

    // @CircuitBreaker
    // Esta configuración solo aplica del application.yml
    @CircuitBreaker(name = "items", fallbackMethod = "getFallBackMethodProduct")
    @GetMapping("/details/{id}")
    public ResponseEntity<?> details2(@PathVariable Long id) {
        Optional<Item> itemOptional = service.findById(id);

        if (itemOptional.isPresent()) {
            return ResponseEntity.ok(itemOptional.get());
        }
        return ResponseEntity.status(404)
                .body(Collections.singletonMap("message", "Producto no existe en el microservicio msvc-products"));
    }

    @CircuitBreaker(name = "items", fallbackMethod = "getFallBackMethodProduct2")
    @TimeLimiter(name = "items")
    @GetMapping("/details2/{id}")
    public CompletableFuture<?> details3(@PathVariable Long id) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Item> itemOptional = service.findById(id);

            if (itemOptional.isPresent()) {
                return ResponseEntity.ok(itemOptional.get());
            }
            return ResponseEntity.status(404)
                    .body(Collections
                            .singletonMap(
                                    "message", "Producto no existe en el microservicio msvc-products"));
        });
    }

    public ResponseEntity<?> getFallBackMethodProduct(Throwable e) {
        System.out.println(e.getMessage());
        logger.error(e.getMessage());

        Product product = new Product();
        product.setCreateAt(LocalDate.now());
        product.setId(1L);
        product.setName("Camara Sony - CortoCircuito");
        product.setPrice(500.00);
        return ResponseEntity.ok(new Item(product, 5));
    }

    public CompletableFuture<?> getFallBackMethodProduct2(Throwable e) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println(e.getMessage());
            logger.error(e.getMessage());

            Product product = new Product();
            product.setCreateAt(LocalDate.now());
            product.setId(1L);
            product.setName("Camara Sony - CortoCircuito");
            product.setPrice(500.00);
            return ResponseEntity.ok(new Item(product, 5));
        });
    }

}

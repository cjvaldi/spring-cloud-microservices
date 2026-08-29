package com.cjvaldi.springcloud.msvc.items.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.cjvaldi.springcloud.msvc.items.models.Item;
import com.cjvaldi.springcloud.msvc.items.services.ItemService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ItemController {

    private final ItemService service;

    // esta anotación @Qualifier("ItemServiceWebClient") es para inyectar el servicio que implementa la interfaz ItemService, en este caso el ItemServiceWebClient, ya que hay otra implementación ItemServiceFeing
    public ItemController(@Qualifier("itemServiceWebClient")ItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<Item> list(@RequestParam(name = "name", required = false) String name,
            @RequestHeader(name = "token-request", required = false)String token) {
                System.out.println("name: " + name);
                System.out.println("token-request: " + token);
        return service.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> details(@PathVariable Long id ){
        Optional<Item> itemOptional = service.findById(id);
        if(itemOptional.isPresent()) {
            return ResponseEntity.ok(itemOptional.get());
        }
         return ResponseEntity.status(404)
        .body(Collections.singletonMap("message", "Producto no existe en el microservicio msvc-products"));
    }
    
}

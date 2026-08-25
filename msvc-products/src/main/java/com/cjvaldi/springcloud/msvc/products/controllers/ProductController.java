package com.cjvaldi.springcloud.msvc.products.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cjvaldi.springcloud.msvc.products.entities.Product;
import com.cjvaldi.springcloud.msvc.products.services.ProductService;

@RestController
// @RequestMapping("/api/products")
@RequestMapping
public class ProductController {

    final private ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<Product> list(){
        return this.service.findAll();
    }

    // opcion alternative a list() para retornar un ResponseEntity
    // @GetMapping
    // public ResponseEntity<?> listResponseEntity(){
    //     return ResponseEntity.ok(this.service.findAll());
    // }

    @GetMapping("/{id}")
    public ResponseEntity<?> details(@PathVariable(name = "id") Long id){
        Optional<Product> productOptional = service.findById(id);
        if(productOptional.isPresent()){
            return ResponseEntity.ok(productOptional.orElseThrow());
        }
        return ResponseEntity.status(404)
        .body(Collections.singletonMap("message", "Producto no existe en nuestra base de datos"));
    }

}

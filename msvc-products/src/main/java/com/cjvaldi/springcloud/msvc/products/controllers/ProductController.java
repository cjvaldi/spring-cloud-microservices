package com.cjvaldi.springcloud.msvc.products.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cjvaldi.libs.msvc.commons.entities.Product;
import com.cjvaldi.springcloud.msvc.products.services.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
// @RequestMapping("/api/products")
@RequestMapping
public class ProductController {

    final private ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<Product> list() {
        return this.service.findAll();
    }

    // opcion alternative a list() para retornar un ResponseEntity
    // @GetMapping
    // public ResponseEntity<?> listResponseEntity(){
    // return ResponseEntity.ok(this.service.findAll());
    // }

    @GetMapping("/{id}")
    public ResponseEntity<?> details(@PathVariable(name = "id") Long id) throws InterruptedException {

        if (id.equals(10L)) {
            //throw new IllegalStateException("Producto no encontrado");
        }

        if (id.equals(7L)) {
            TimeUnit.SECONDS.sleep(3L);
        }

        Optional<Product> productOptional = service.findById(id);
        if (productOptional.isPresent()) {
            return ResponseEntity.ok(productOptional.orElseThrow());
        }
        return ResponseEntity.status(404)
                .body(Collections.singletonMap("message", "Producto no existe en nuestra base de datos"));
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@RequestBody Product product, @PathVariable(name = "id") Long id) {
        Optional<Product> productOptional = service.findById(id);
        if (productOptional.isPresent()) {
            Product productDB = productOptional.orElseThrow();
            productDB.setName(product.getName());
            productDB.setPrice(product.getPrice());
            productDB.setCreateAt(product.getCreateAt());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(productDB));
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        Optional<Product> productOptional = service.findById(id);
        if (productOptional.isPresent()) {
            this.service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}

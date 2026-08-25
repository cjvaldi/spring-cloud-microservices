package com.cjvaldi.springcloud.msvc.items.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cjvaldi.springcloud.msvc.items.models.Product;

@FeignClient(name = "msvc-products")
public interface ProductFeingClient {

    @GetMapping
    List<Product>findAll();

    @GetMapping("/{id}")
    Product details(@PathVariable Long id);
    
}

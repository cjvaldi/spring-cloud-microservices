package com.cjvaldi.springcloud.msvc.items.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cjvaldi.springcloud.msvc.items.clients.ProductFeingClient;
import com.cjvaldi.springcloud.msvc.items.models.Item;
import com.cjvaldi.springcloud.msvc.items.models.Product;

import feign.FeignException;

@Service
public class ItemServiceFeing implements ItemService {

    
    private final ProductFeingClient client;

    public ItemServiceFeing(ProductFeingClient client) {
        this.client = client;
    }

    @Override
    public List<Item> findAll() {
        return client.findAll()
        .stream()
        .map(product -> new Item(product, new Random().nextInt(10)+1))
        .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> findById(Long id) {
        try {
            Product product = client.details(id);
            return Optional.of(new Item(product, new Random().nextInt(10)+1));
        } catch (FeignException e) {
            return Optional.empty();
        }
    }

}


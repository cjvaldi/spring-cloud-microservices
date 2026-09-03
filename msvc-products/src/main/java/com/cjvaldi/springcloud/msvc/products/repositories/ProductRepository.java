package com.cjvaldi.springcloud.msvc.products.repositories;

import org.springframework.data.repository.CrudRepository;

import com.cjvaldi.libs.msvc.commons.entities.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

}

package com.cjvaldi.springcloud.msvc.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan({ "com.cjvaldi.libs.msvc.commons.entities",
		"com.cjvaldi.springcloud.msvc.products.entities" })
public class MsvcProductsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcProductsApplication.class, args);
	}

}

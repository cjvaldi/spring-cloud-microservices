package com.cjvaldi.springcloud.msvc.products;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled ("Deshabilitado en CI: el contexto completo requiere Eureka/Config Server y MySQL activo. La persistencia se valida con Testcontainers en ProductRepositoryTest")
@SpringBootTest
class MscvProductsApplicationTests {

	@Test
	void contextLoads() {
	}

}

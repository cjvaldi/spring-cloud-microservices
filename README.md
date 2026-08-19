# 🚀 Spring Cloud Microservices Ecosystem

Ecosistema de microservicios desarrollado con **Java 21+**, **Spring Boot 3** y el stack completo de **Spring Cloud**. El repositorio implementa una arquitectura distribuida orientada a alta disponibilidad, tolerancia a fallos, autenticación centralizada y despliegue contenerizado.

---

## 🛠️ Stack Tecnológico

- **Lenguaje:** Java 21+
- **Framework Base:** Spring Boot 3.x
- **Descubrimiento y Registro:** Spring Cloud Netflix Eureka Server
- **Routing & API Gateway:** Spring Cloud Gateway
- **Balanceo de Carga:** Spring Cloud LoadBalancer
- **Tolerancia a Fallos:** Resilience4J (Circuit Breaker, RateLimiter, Retry)
- **Seguridad:** Spring Authorization Server (OAuth 2.1 / OpenID Connect)
- **Comunicación HTTP Declarativa:** Spring Cloud OpenFeign
- **Contenerización & Despliegue:** Docker, Docker Compose, AWS EC2

---

## 📐 Arquitectura de Microservicios

```text
spring-cloud-microservices/
├── config-server/             # Configuración centralizada (Spring Cloud Config)
├── eureka-server/             # Service Discovery (Netflix Eureka)
├── gateway-server/            # API Gateway unificado (Filtros y enrutamiento)
├── auth-server/               # Servidor de Autorización OAuth 2.1
├── msvc-products/             # Microservicio de catálogo de productos
├── msvc-items/                # Microservicio de items / consumo con Feign y Resilience4J
├── docker-compose.yml         # Orquestación de infraestructura local
└── README.md
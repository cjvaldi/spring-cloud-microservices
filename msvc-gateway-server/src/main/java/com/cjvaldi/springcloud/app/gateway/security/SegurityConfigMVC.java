// package com.cjvaldi.springcloud.app.gateway.security;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.web.SecurityFilterChain;

// import static org.springframework.security.config.Customizer.withDefaults;

// La clase SecurityConfig para Spring MVC (Servlet)

// @Configuration
// public class SegurityConfigMVC {

//     @Bean
//     SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         return http.authorizeHttpRequests((authz) -> {
//             authz
//                     .requestMatchers("/authorized", "/logout").permitAll()
//                     .requestMatchers(HttpMethod.GET, "/api/products", "/api/items", "/api/users").permitAll()
//                     .requestMatchers(HttpMethod.GET, "/api/products/{id}", "/api/items/{id}", "/api/users/{id}")
//                     .hasAnyRole("ADMIN", "USER")
//                     .requestMatchers("/api/products/**", "/api/items/**", "/api/users/**").hasRole("ADMIN")
//                     .anyRequest().authenticated();
//         })
//                 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                 .csrf(csrf -> csrf.disable())
//                 .oauth2Login(login -> login.loginPage("/oauth2/authorization/client-app"))
//                 .oauth2Client(withDefaults())
//                 .oauth2ResourceServer(withDefaults())
//                 .build();
//     }

// }

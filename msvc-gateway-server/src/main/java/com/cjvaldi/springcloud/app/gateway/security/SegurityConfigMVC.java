// La clase SecurityConfig para Spring MVC (Servlet)

// package com.cjvaldi.springcloud.app.gateway.security;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.convert.converter.Converter;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.authentication.AbstractAuthenticationToken;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.oauth2.jwt.Jwt;
// import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
// import org.springframework.security.web.SecurityFilterChain;

// import static org.springframework.security.config.Customizer.withDefaults;

// import java.util.Collection;
// import java.util.stream.Collectors;

// @Configuration
// public class SegurityConfig {

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
//                 .csrf(csrf -> csrf.disable())
//                 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                 .oauth2Login(login -> login.loginPage("/oauth2/authorization/client-app"))
//                 .oauth2Client(withDefaults())
//                 .oauth2ResourceServer(oauth2 -> oauth2.jwt(
//                         jwt -> jwt.jwtAuthenticationConverter(new Converter<Jwt, AbstractAuthenticationToken>() {

//                             @Override
//                             public AbstractAuthenticationToken convert(Jwt source) {
//                                 Collection<String> roles = source.getClaimAsStringList("roles");
//                                 Collection<GrantedAuthority> authorities = roles.stream()
//                                         .map(SimpleGrantedAuthority::new)
//                                         .collect(Collectors.toList());

//                                 return new JwtAuthenticationToken(source, authorities);
//                             }
//                         })))
//                 .build();
//     }
// }

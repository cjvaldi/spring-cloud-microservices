package com.cjvaldi.springcloud.msvc.oauth.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

// Enable Spring Security's web security support and provide the Spring MVC integration
// https://docs.spring.io/spring-authorization-server/reference/getting-started.html

@Configuration
public class SecurityConfig {

	// @Autowired 
	// private PasswordEncoder passwordEncoder;

	@Bean
	@Order(1)
	SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

		// Instanciación directa compatible con versiones anteriores a 1.2
		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

		http
				.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
				.with(authorizationServerConfigurer, (authorizationServer) -> authorizationServer
						.oidc(Customizer.withDefaults()) // Enable OpenID Connect 1.0
				)
				.authorizeHttpRequests((authorize) -> authorize
						.anyRequest().authenticated())
				.exceptionHandling((exceptions) -> exceptions
						.defaultAuthenticationEntryPointFor(
								new LoginUrlAuthenticationEntryPoint("/login"),
								new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));

		return http.build();
	}

	@Bean
	@Order(2)
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
			throws Exception {
		http
				.authorizeHttpRequests((authorize) -> authorize
						.anyRequest().authenticated())
				// Form login handles the redirect to the login page from the
				// authorization server filter chain
				.csrf(csrf -> csrf.disable())
				.formLogin(Customizer.withDefaults());

		return http.build();
	}

	// Por defecto, Spring Authorization Server solo incluye claims estándar (sub,
	// aud, iss, exp, scope). En este paso inyectaremos una lista de roles o
	// authorities del usuario autenticado (por ejemplo, ROLE_USER, ROLE_ADMIN)
	// dentro del payload del JWT para que los microservicios downstream
	// (msvc-gateway-server, msvc-items, msvc-products) puedan autorizar por roles.
	@Bean
	OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
		return context -> {
			if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
				Authentication principal = context.getPrincipal();
				List<String> roles = principal.getAuthorities().stream()
						.map(GrantedAuthority::getAuthority)
						.toList();

				context.getClaims()
				.claim("roles", roles)
				.claim("data","data adicional en el token");

			}
		};
	}

	// Solo para pruebas
	@Bean
	UserDetailsService userDetailsService() {
		UserDetails userDetails = User.builder()
				.username("cristian")
				.password("{noop}12345")
				.roles("USER")
				.build();
		UserDetails admin = User.builder()
				.username("admin")
				.password("{noop}12345")
				.roles("USER", "ADMIN")
				.build();

		return new InMemoryUserDetailsManager(userDetails, admin);
	}



	@Bean
	RegisteredClientRepository registeredClientRepository() {
		RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("gateway-app")
				.clientSecret("{noop}12345")
				//.clientSecret(passwordEncoder.encode("12345"))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS) // 👈 Añade esta línea
				.redirectUri("http://127.0.0.1:8090/login/oauth2/code/client-app")
				.redirectUri("http://127.0.0.1:8090/authorized")
				.redirectUri("https://oauth.pstmn.io/v1/callback") // 👈 Añade esta para usar Postman nativ
				.postLogoutRedirectUri("http://127.0.0.1:8090/logout")
				.scope(OidcScopes.OPENID)
				.scope(OidcScopes.PROFILE)
				.scope("write") // para pruebas
				.scope("read")
				.clientSettings(ClientSettings.builder()
						.requireAuthorizationConsent(false)
						.requireProofKey(false) // 👈 El método exacto es requireProofKey
						.build())
				.build();

		return new InMemoryRegisteredClientRepository(oidcClient);
	}

	@Bean
	JWKSource<SecurityContext> jwkSource() {
		KeyPair keyPair = generateRsaKey();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAKey rsaKey = new RSAKey.Builder(publicKey)
				.privateKey(privateKey)
				.keyID(UUID.randomUUID().toString())
				.build();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return new ImmutableJWKSet<>(jwkSet);
	}

	private static KeyPair generateRsaKey() {
		KeyPair keyPair;
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();
		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
		return keyPair;
	}

	@Bean
	JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	@Bean
	AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}
}

/**
 * Ruta 1: Probar el flujo completo directo en Postman (La vía más rápida para
 * validar)
 * Permite obtener el token sin lidiar con redirecciones manuales ni copiado de
 * códigos.
 * 
 * En Postman, crea una nueva pestaña de petición.
 * 
 * Ve a la pestaña Authorization y en el desplegable Type elige OAuth 2.0.
 * 
 * Baja hasta la sección Configure New Token e ingresa estos valores:
 * 
 * Token Name: Token Authorization Code
 * 
 * Grant Type: Authorization Code (With PKCE)
 * 
 * Callback URL:
 * [https://oauth.pstmn.io/v1/callback](https://oauth.pstmn.io/v1/callback)
 * 
 * (Asegúrate de haber añadido esta URL exacta en las redirectUri de tu
 * RegisteredClientRepository)
 * 
 * Auth URL:
 * [http://127.0.0.1:9100/oauth2/authorize](http://127.0.0.1:9100/oauth2/authorize)
 * 
 * Access Token URL:
 * [http://127.0.0.1:9100/oauth2/token](http://127.0.0.1:9100/oauth2/token)
 * 
 * Client ID: gateway-app
 * 
 * Client Secret: 12345
 * 
 * Code Challenge Method: S256
 * 
 * Scope: openid profile
 * 
 * Client Authentication: Send as Basic Auth header
 * 
 * Haz clic en el botón naranja Get New Access Token.
 * 
 * Se abrirá una ventana de navegador integrada en Postman. Introduce las
 * credenciales de usuario:
 * 
 * Username: cristian
 * 
 * Password: 12345
 * 
 * Postman procesará el código y el PKCE de inmediato y te mostrará el token
 * recibido con access_token e id_token. Haz clic en Use Token.
 * 
 * Solicitar el code desde navegador o postman
 * http://127.0.0.1:9100/oauth2/authorize?response_type=code&client_id=gateway-app&redirect_uri=http://127.0.0.1:8090/authorized&scope=openid%20profile
 */
